package com.lostfound.member2_items.util;

import com.lostfound.config.AppConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Utility for Secure Image File Uploads.
 * Enforces file size limits, MIME type inspection, extension whitelist,
 * and random UUID renaming to prevent arbitrary file upload vulnerabilities.
 */
public class ImageUploadUtil {

    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    private static final Map<String, String> ALLOWED_FORMATS = Map.of(
            "jpg", "jpeg",
            "jpeg", "jpeg",
            "jfif", "jpeg",
            "png", "png",
            "webp", "webp"
    );

    private ImageUploadUtil() {
        // Utility class
    }

    public static String getUploadDirectory(ServletContext context) {
        String configuredDirectory = AppConfig.getUploadDir();
        File configuredPath = new File(configuredDirectory);
        if (configuredPath.isAbsolute()) {
            return configuredPath.getAbsolutePath();
        }

        String containerBase = System.getProperty("catalina.base");
        if (containerBase != null && !containerBase.isBlank()) {
            return new File(containerBase, configuredDirectory).getAbsolutePath();
        }

        if (context != null) {
            String webPath = "/" + configuredDirectory.replace(File.separatorChar, '/');
            String deployedPath = context.getRealPath(webPath);
            if (deployedPath != null) {
                return deployedPath;
            }
        }

        return new File(System.getProperty("user.dir"), configuredDirectory).getAbsolutePath();
    }

    /**
     * Validates and saves an uploaded file Part.
     *
     * @param part The uploaded Part from HttpServletRequest
     * @param uploadDirRealPath The absolute real path to upload directory (e.g., .../uploads/items)
     * @return The relative web path (e.g. "uploads/items/abc.jpg") or null if no file provided
     * @throws IllegalArgumentException on security or format validation error
     * @throws IOException on I/O error
     */
    public static String saveUploadedImage(Part part, String uploadDirRealPath) throws IOException {
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null || part.getSubmittedFileName().trim().isEmpty()) {
            return null;
        }

        // 1. Validate file size
        if (part.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Uploaded file exceeds the maximum allowed size of 5 MB.");
        }

        // 2. Extract and sanitize file extension
        String submittedFilename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String extension = "";
        int dotIndex = submittedFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < submittedFilename.length() - 1) {
            extension = submittedFilename.substring(dotIndex + 1).toLowerCase();
        }

        String expectedFormat = ALLOWED_FORMATS.get(extension);
        if (expectedFormat == null) {
            throw new IllegalArgumentException("Invalid file format ." + extension + ". Allowed formats: JPG, JPEG, JFIF, PNG, WEBP.");
        }

        // Check both the browser-provided type and the actual file signature.
        String contentType = part.getContentType();
        String cleanType = contentType == null
                ? ""
                : contentType.split(";")[0].trim().toLowerCase(Locale.ROOT);
        if (!cleanType.isEmpty()
                && !"application/octet-stream".equals(cleanType)
                && !isCompatibleMimeType(expectedFormat, cleanType)) {
            throw new IllegalArgumentException("Invalid MIME type (" + contentType + "). Please upload a JPG, JPEG, JFIF, PNG, or WEBP image.");
        }

        byte[] bytes;
        try (InputStream inputStream = part.getInputStream()) {
            bytes = inputStream.readAllBytes();
        }
        if (!matchesImageSignature(bytes, expectedFormat)) {
            throw new IllegalArgumentException("The uploaded file content does not match its ." + extension + " image format.");
        }

        // Ensure destination directory exists.
        if (uploadDirRealPath == null || uploadDirRealPath.trim().isEmpty()) {
            uploadDirRealPath = System.getProperty("java.io.tmpdir") + File.separator + "lostfound_uploads";
        }
        Path uploadDirectory = Paths.get(uploadDirRealPath);
        Files.createDirectories(uploadDirectory);

        // Keep JFIF files under a broadly recognized JPEG extension.
        String safeFileName = UUID.randomUUID().toString() + "." + ("jfif".equals(extension) ? "jpg" : extension);
        Path targetPath = uploadDirectory.resolve(safeFileName);

        Files.write(targetPath, bytes);

        // Return relative database path
        return "uploads/items/" + safeFileName;
    }

    private static boolean isCompatibleMimeType(String format, String mimeType) {
        return switch (format) {
            case "jpeg" -> "image/jpeg".equals(mimeType)
                    || "image/jpg".equals(mimeType)
                    || "image/pjpeg".equals(mimeType)
                    || "image/jfif".equals(mimeType);
            case "png" -> "image/png".equals(mimeType) || "image/x-png".equals(mimeType);
            case "webp" -> "image/webp".equals(mimeType);
            default -> false;
        };
    }

    private static boolean matchesImageSignature(byte[] bytes, String format) {
        return switch (format) {
            case "jpeg" -> bytes.length >= 3
                    && (bytes[0] & 0xff) == 0xff
                    && (bytes[1] & 0xff) == 0xd8
                    && (bytes[2] & 0xff) == 0xff;
            case "png" -> bytes.length >= 8
                    && (bytes[0] & 0xff) == 0x89
                    && bytes[1] == 'P'
                    && bytes[2] == 'N'
                    && bytes[3] == 'G'
                    && (bytes[4] & 0xff) == 0x0d
                    && (bytes[5] & 0xff) == 0x0a
                    && (bytes[6] & 0xff) == 0x1a
                    && (bytes[7] & 0xff) == 0x0a;
            case "webp" -> bytes.length >= 12
                    && bytes[0] == 'R'
                    && bytes[1] == 'I'
                    && bytes[2] == 'F'
                    && bytes[3] == 'F'
                    && bytes[8] == 'W'
                    && bytes[9] == 'E'
                    && bytes[10] == 'B'
                    && bytes[11] == 'P';
            default -> false;
        };
    }
}
