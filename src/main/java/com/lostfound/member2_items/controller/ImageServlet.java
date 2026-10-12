package com.lostfound.member2_items.controller;

import com.lostfound.member2_items.util.ImageUploadUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

@WebServlet("/uploads/items/*")
public class ImageServlet extends HttpServlet {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "jfif", "png", "webp");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fileName = request.getPathInfo();
        if (fileName == null || fileName.length() < 2 || fileName.indexOf('/', 1) >= 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        fileName = fileName.substring(1);
        int extensionSeparator = fileName.lastIndexOf('.');
        if (extensionSeparator < 1
                || !ALLOWED_EXTENSIONS.contains(fileName.substring(extensionSeparator + 1).toLowerCase(Locale.ROOT))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path image = null;

        // Try candidate directory locations
        String primaryUploadDir = ImageUploadUtil.getUploadDirectory(getServletContext());
        if (primaryUploadDir != null) {
            Path p = Path.of(primaryUploadDir).resolve(fileName).normalize();
            if (Files.isRegularFile(p)) {
                image = p;
            }
        }

        if (image == null) {
            String deployedPath = getServletContext().getRealPath("/uploads/items/" + fileName);
            if (deployedPath != null) {
                Path p = Path.of(deployedPath).normalize();
                if (Files.isRegularFile(p)) {
                    image = p;
                }
            }
        }

        if (image == null) {
            Path p = Path.of(System.getProperty("user.dir"), "src", "main", "webapp", "uploads", "items", fileName).normalize();
            if (Files.isRegularFile(p)) {
                image = p;
            }
        }

        if (image == null) {
            Path p = Path.of(System.getProperty("user.dir"), "uploads", "items", fileName).normalize();
            if (Files.isRegularFile(p)) {
                image = p;
            }
        }

        if (image == null) {
            Path p = Path.of(System.getProperty("java.io.tmpdir"), "lostfound_uploads", fileName).normalize();
            if (Files.isRegularFile(p)) {
                image = p;
            }
        }

        if (image == null || !Files.isRegularFile(image)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = null;
        try {
            contentType = Files.probeContentType(image);
        } catch (Exception ignored) {}

        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            String ext = fileName.substring(extensionSeparator + 1).toLowerCase(Locale.ROOT);
            if ("png".equals(ext)) {
                contentType = "image/png";
            } else if ("webp".equals(ext)) {
                contentType = "image/webp";
            } else {
                contentType = "image/jpeg";
            }
        }

        response.setContentType(contentType);
        response.setHeader("Cache-Control", "public, max-age=86400");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentLengthLong(Files.size(image));
        Files.copy(image, response.getOutputStream());
    }
}
