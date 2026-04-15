package com.resumeanalyzer.aianalyzer.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.bind.annotation.CrossOrigin;
@RestController
@CrossOrigin(
    origins = "*",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class ResumeController {

    @PostMapping("/analyze")
    public String analyze(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "File is empty ❌";
        }

        try {
            PDDocument doc = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc).toLowerCase();

            text = text.replaceAll("[^a-zA-Z0-9#.+ ]", " ");
            doc.close();

            String[] backend = {
                "java", "spring", "spring boot", "node",
                "c#", "dotnet", "php", "golang", "ruby"
            };

            String[] frontend = {
                "html", "css", "javascript", "react", "angular",
                "vue", "next js", "bootstrap", "tailwind"
            };

            String[] database = {
                "sql", "mysql", "postgresql", "mongodb", "oracle"
            };

            String[] tools = {
                "git", "github", "docker", "kubernetes", "aws",
                "azure", "gcp", "jenkins"
            };

            StringBuilder result = new StringBuilder();

            int score = 0;
            int backendCount = 0;
            int frontendCount = 0;
            int databaseCount = 0;
            int toolsCount = 0;

            // =========================
            // SKILLS SECTION
            // =========================

            StringBuilder report = new StringBuilder();

            // Backend
            report.append("💻 Backend Skills:\n");
            for (String skill : backend) {
                if (match(text, skill)) {
                    report.append("✔ ").append(skill).append("\n");
                    score++;
                    backendCount++;
                }
            }
            report.append("\n");

            // Frontend
            report.append("🎨 Frontend Skills:\n");
            for (String skill : frontend) {
                if (match(text, skill)) {
                    report.append("✔ ").append(skill).append("\n");
                    score++;
                    frontendCount++;
                }
            }
            report.append("\n");

            // Database
            report.append("🗄 Database Skills:\n");
            for (String skill : database) {
                if (match(text, skill)) {
                    report.append("✔ ").append(skill).append("\n");
                    score++;
                    databaseCount++;
                }
            }
            report.append("\n");

            // Tools
            report.append("⚙ Tools & Platforms:\n");
            for (String skill : tools) {
                if (match(text, skill)) {
                    report.append("✔ ").append(skill).append("\n");
                    score++;
                    toolsCount++;
                }
            }

            // =========================
            // ROLE LOGIC (IMPROVED)
            // =========================

            String role;

            if (backendCount >= 2 && frontendCount >= 2) {
                role = "Full Stack Developer";
            } else if (backendCount >= 2) {
                role = "Backend Developer";
            } else if (frontendCount >= 2) {
                role = "Frontend Developer";
            } else if (containsWord(text, "python")) {
                role = "Python Developer";
            } else if (containsWord(text, "aws") || containsWord(text, "azure")) {
                role = "Cloud Engineer";
            } else {
                role = "General Developer";
            }

            // =========================
            // SCORE
            // =========================

            int finalScore = Math.min(10, score);

            // =========================
            // FINAL OUTPUT (VERY CLEAN)
            // =========================

            result.append("⭐ AI Score: ").append(finalScore).append("/10\n\n");
            result.append("💼 Suggested Role: ").append(role).append("\n\n");
            result.append("🔍 Resume Analysis Report\n\n");
            result.append(report);

            return result.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // =========================
    // MATCHING FUNCTIONS
    // =========================

    private boolean match(String text, String skill) {
        if (skill.contains(" ")) {
            return text.contains(skill);
        } else {
            return containsWord(text, skill);
        }
    }

    private boolean containsWord(String text, String word) {
        return text.matches(".*\\b" + java.util.regex.Pattern.quote(word) + "\\b.*");
    }
}
