package com.security.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ClientApi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class EnhancedReportGenerator {

    private static final String REPORTS_DIR = "reports";
    private static final String FINDINGS_FILE = "target/custom-findings.json";
    private static ClientApi zapApi;
    private static List<CustomFinding> customFindings = new ArrayList<>();

    public static void main(String[] args) {
        try {
            System.out.println("╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║    Generating Enhanced Security Report with Custom Findings  ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

            // Load custom findings
            loadCustomFindings();

            // Initialize ZAP API
            String zapHost = System.getProperty("zap.proxy.host", "localhost");
            int zapPort = Integer.parseInt(System.getProperty("zap.proxy.port", "8080"));
            zapApi = new ClientApi(zapHost, zapPort);

            // Create reports directory
            new File(REPORTS_DIR).mkdirs();

            // Generate reports
            generateHtmlReport();
            generateJsonReport();
            generateMarkdownReport();
            generateIndexPage();

            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║              ✓ Reports Generated Successfully!                ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.println("\n📊 View reports at: " + new File(REPORTS_DIR).getAbsolutePath());
            System.out.println("   • index.html           - Main dashboard");
            System.out.println("   • security-report.html - Full detailed report");
            System.out.println("   • security-report.json - JSON format");
            System.out.println("   • security-report.md   - Markdown format\n");

        } catch (Exception e) {
            System.err.println("Error generating reports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadCustomFindings() {
        try {
            File findingsFile = new File(FINDINGS_FILE);
            if (findingsFile.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                CustomFinding[] findings = mapper.readValue(findingsFile, CustomFinding[].class);
                customFindings = Arrays.asList(findings);
                System.out.println("✓ Loaded " + customFindings.size() + " custom findings from tests");
            } else {
                System.out.println("⚠ No custom findings file found at: " + FINDINGS_FILE);
            }
        } catch (Exception e) {
            System.err.println("Error loading custom findings: " + e.getMessage());
        }
    }

    private static void generateHtmlReport() throws Exception {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Security Assessment Report - Bosta APIs</title>\n");
        html.append("    <style>\n");
        html.append(getStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        // Header
        html.append("    <header>\n");
        html.append("        <div class=\"container\">\n");
        html.append("            <h1>🔒 Enhanced Security Assessment Report</h1>\n");
        html.append("            <p class=\"subtitle\">Automated Security Testing for Bosta APIs</p>\n");
        html.append("            <p class=\"date\">Generated: ").append(getCurrentDateTime()).append("</p>\n");
        html.append("        </div>\n");
        html.append("    </header>\n");

        html.append("    <div class=\"container\">\n");

        // Executive Summary
        generateExecutiveSummary(html);

        // Custom Findings Section (PROMINENT)
        generateCustomFindingsSection(html);

        // APIs Tested
        generateApisTestedSection(html);

        // ZAP Findings
        generateZapFindingsSection(html);

        // Test Coverage
        generateTestCoverageSection(html);

        html.append("    </div>\n");

        // Footer
        html.append("    <footer>\n");
        html.append("        <div class=\"container\">\n");
        html.append("            <p>Generated by Enhanced DAST Security Pipeline</p>\n");
        html.append("        </div>\n");
        html.append("    </footer>\n");

        html.append("</body>\n");
        html.append("</html>");

        // Write to file
        try (FileWriter writer = new FileWriter(REPORTS_DIR + "/security-report.html")) {
            writer.write(html.toString());
        }

        System.out.println("✓ HTML report generated");
    }

    private static void generateExecutiveSummary(StringBuilder html) {
        html.append("        <section class=\"summary\">\n");
        html.append("            <h2>📊 Executive Summary</h2>\n");
        html.append("            <div class=\"summary-grid\">\n");

        Map<String, Integer> customSeverity = countCustomBySeverity();
        List<VulnerabilityData> zapVulns = getVulnerabilitiesFromZap();
        Map<String, Integer> zapSeverity = countBySeverity(zapVulns);

        int totalHigh = customSeverity.getOrDefault("HIGH", 0) +
                customSeverity.getOrDefault("CRITICAL", 0) +
                zapSeverity.getOrDefault("High", 0);
        int totalMedium = customSeverity.getOrDefault("MEDIUM", 0) +
                zapSeverity.getOrDefault("Medium", 0);
        int totalLow = customSeverity.getOrDefault("LOW", 0) +
                zapSeverity.getOrDefault("Low", 0);
        int totalInfo = customSeverity.getOrDefault("INFO", 0) +
                zapSeverity.getOrDefault("Informational", 0);

        html.append("                <div class=\"stat-card critical\">\n");
        html.append("                    <div class=\"stat-number\">").append(totalHigh).append("</div>\n");
        html.append("                    <div class=\"stat-label\">Critical/High</div>\n");
        html.append("                    <div class=\"stat-detail\">Custom: ").append(customSeverity.getOrDefault("HIGH", 0))
                .append(" | ZAP: ").append(zapSeverity.getOrDefault("High", 0)).append("</div>\n");
        html.append("                </div>\n");

        html.append("                <div class=\"stat-card medium\">\n");
        html.append("                    <div class=\"stat-number\">").append(totalMedium).append("</div>\n");
        html.append("                    <div class=\"stat-label\">Medium</div>\n");
        html.append("                    <div class=\"stat-detail\">Custom: ").append(customSeverity.getOrDefault("MEDIUM", 0))
                .append(" | ZAP: ").append(zapSeverity.getOrDefault("Medium", 0)).append("</div>\n");
        html.append("                </div>\n");

        html.append("                <div class=\"stat-card low\">\n");
        html.append("                    <div class=\"stat-number\">").append(totalLow).append("</div>\n");
        html.append("                    <div class=\"stat-label\">Low</div>\n");
        html.append("                    <div class=\"stat-detail\">Custom: ").append(customSeverity.getOrDefault("LOW", 0))
                .append(" | ZAP: ").append(zapSeverity.getOrDefault("Low", 0)).append("</div>\n");
        html.append("                </div>\n");

        html.append("                <div class=\"stat-card info\">\n");
        html.append("                    <div class=\"stat-number\">").append(totalInfo).append("</div>\n");
        html.append("                    <div class=\"stat-label\">Informational</div>\n");
        html.append("                    <div class=\"stat-detail\">Custom: ").append(customSeverity.getOrDefault("INFO", 0))
                .append(" | ZAP: ").append(zapSeverity.getOrDefault("Informational", 0)).append("</div>\n");
        html.append("                </div>\n");

        html.append("            </div>\n");
        html.append("            <div class=\"summary-note\">\n");
        html.append("                <strong>Total Findings: ").append(customFindings.size() + zapVulns.size()).append("</strong>\n");
        html.append("                <span>(").append(customFindings.size()).append(" from custom tests, ")
                .append(zapVulns.size()).append(" from OWASP ZAP)</span>\n");
        html.append("            </div>\n");
        html.append("        </section>\n");
    }

    private static void generateCustomFindingsSection(StringBuilder html) {
        html.append("        <section class=\"custom-findings\">\n");
        html.append("            <h2>🎯 Custom Test Findings (Your Tests)</h2>\n");

        if (customFindings.isEmpty()) {
            html.append("            <div class=\"info-box\">\n");
            html.append("                <p>✓ No custom security issues detected by your tests!</p>\n");
            html.append("            </div>\n");
        } else {
            html.append("            <div class=\"findings-summary\">\n");
            html.append("                <p>Found <strong>").append(customFindings.size())
                    .append(" security issues</strong> through custom testing:</p>\n");
            html.append("            </div>\n");

            // Group by API
            Map<String, List<CustomFinding>> groupedByApi = new HashMap<>();
            for (CustomFinding finding : customFindings) {
                groupedByApi.computeIfAbsent(finding.api, k -> new ArrayList<>()).add(finding);
            }

            for (Map.Entry<String, List<CustomFinding>> entry : groupedByApi.entrySet()) {
                html.append("            <div class=\"api-findings\">\n");
                html.append("                <h3>").append(escapeHtml(entry.getKey())).append("</h3>\n");

                for (CustomFinding finding : entry.getValue()) {
                    String severityClass = getSeverityClass(finding.severity);
                    html.append("                <div class=\"vulnerability ").append(severityClass).append("\">\n");
                    html.append("                    <div class=\"vuln-header\">\n");
                    html.append("                        <h4>").append(escapeHtml(finding.issue)).append("</h4>\n");
                    html.append("                        <span class=\"severity-badge ").append(severityClass).append("\">")
                            .append(finding.severity).append("</span>\n");
                    html.append("                    </div>\n");
                    html.append("                    <div class=\"vuln-details\">\n");
                    html.append("                        <p><strong>Test:</strong> <code>").append(escapeHtml(finding.testName)).append("</code></p>\n");
                    html.append("                        <p><strong>Detected:</strong> ").append(finding.timestamp).append("</p>\n");
                    html.append("                        <p><strong>Source:</strong> Custom Security Test</p>\n");
                    html.append("                    </div>\n");
                    html.append("                </div>\n");
                }
                html.append("            </div>\n");
            }
        }
        html.append("        </section>\n");
    }

    private static void generateApisTestedSection(StringBuilder html) {
        html.append("        <section class=\"apis-tested\">\n");
        html.append("            <h2>🎯 APIs Tested</h2>\n");
        html.append("            <div class=\"api-list\">\n");
        html.append("                <div class=\"api-item\">\n");
        html.append("                    <h3>1. Create Pickup API</h3>\n");
        html.append("                    <p><code>POST /api/v2/pickups</code></p>\n");
        html.append("                    <p>Creates pickup requests for delivery agents</p>\n");
        html.append("                    <p class=\"api-stats\">Tests: 10 | Findings: ")
                .append(countFindingsForApi("Create Pickup API")).append("</p>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"api-item\">\n");
        html.append("                    <h3>2. Update Bank Info API</h3>\n");
        html.append("                    <p><code>POST /api/v2/businesses/add-bank-info</code></p>\n");
        html.append("                    <p>Updates business bank account details</p>\n");
        html.append("                    <p class=\"api-stats\">Tests: 12 | Findings: ")
                .append(countFindingsForApi("Update Bank Info API")).append("</p>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"api-item\">\n");
        html.append("                    <h3>3. Forget Password API</h3>\n");
        html.append("                    <p><code>POST /api/v2/users/forget-password</code></p>\n");
        html.append("                    <p>Sends password reset link to users</p>\n");
        html.append("                    <p class=\"api-stats\">Tests: 13 | Findings: ")
                .append(countFindingsForApi("Forget Password API")).append("</p>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </section>\n");
    }

    private static void generateZapFindingsSection(StringBuilder html) {
        html.append("        <section class=\"zap-findings\">\n");
        html.append("            <h2>🕷️ OWASP ZAP Findings</h2>\n");

        List<VulnerabilityData> vulnerabilities = getVulnerabilitiesFromZap();

        if (vulnerabilities.isEmpty()) {
            html.append("            <div class=\"info-box\">\n");
            html.append("                <p>✓ No vulnerabilities detected by OWASP ZAP automated scanning!</p>\n");
            html.append("            </div>\n");
        } else {
            for (VulnerabilityData vuln : vulnerabilities) {
                html.append("            <div class=\"vulnerability ").append(getSeverityClass(vuln.risk)).append("\">\n");
                html.append("                <div class=\"vuln-header\">\n");
                html.append("                    <h4>").append(escapeHtml(vuln.name)).append("</h4>\n");
                html.append("                    <span class=\"severity-badge ").append(getSeverityClass(vuln.risk)).append("\">")
                        .append(vuln.risk).append("</span>\n");
                html.append("                </div>\n");
                html.append("                <div class=\"vuln-details\">\n");
                html.append("                    <p><strong>URL:</strong> <code>").append(escapeHtml(vuln.url)).append("</code></p>\n");
                html.append("                    <p><strong>Description:</strong> ").append(escapeHtml(vuln.description)).append("</p>\n");
                if (!vuln.solution.isEmpty()) {
                    html.append("                    <p><strong>Solution:</strong> ").append(escapeHtml(vuln.solution)).append("</p>\n");
                }
                if (!vuln.reference.isEmpty()) {
                    html.append("                    <p><strong>Reference:</strong> ").append(escapeHtml(vuln.reference)).append("</p>\n");
                }
                html.append("                    <p><strong>Source:</strong> OWASP ZAP Automated Scan</p>\n");
                html.append("                </div>\n");
                html.append("            </div>\n");
            }
        }
        html.append("        </section>\n");
    }

    private static void generateTestCoverageSection(StringBuilder html) {
        html.append("        <section class=\"test-coverage\">\n");
        html.append("            <h2>✅ Test Coverage</h2>\n");
        html.append("            <div class=\"coverage-grid\">\n");
        html.append("                <div class=\"coverage-column\">\n");
        html.append("                    <h3>Custom Tests</h3>\n");
        html.append("                    <ul class=\"test-list\">\n");
        html.append("                        <li>✓ Authentication & Authorization Testing</li>\n");
        html.append("                        <li>✓ SQL Injection Testing (11 payloads)</li>\n");
        html.append("                        <li>✓ NoSQL Injection Testing (5 payloads)</li>\n");
        html.append("                        <li>✓ Cross-Site Scripting (8 XSS payloads)</li>\n");
        html.append("                        <li>✓ Input Validation Testing</li>\n");
        html.append("                        <li>✓ Rate Limiting Testing</li>\n");
        html.append("                        <li>✓ IDOR Vulnerability Testing</li>\n");
        html.append("                        <li>✓ Mass Assignment Testing</li>\n");
        html.append("                        <li>✓ Token Manipulation Testing</li>\n");
        html.append("                        <li>✓ OTP Bypass Testing</li>\n");
        html.append("                    </ul>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"coverage-column\">\n");
        html.append("                    <h3>OWASP ZAP Scans</h3>\n");
        html.append("                    <ul class=\"test-list\">\n");
        html.append("                        <li>✓ Spider Scan (Crawling)</li>\n");
        html.append("                        <li>✓ Active Scan (Automated)</li>\n");
        html.append("                        <li>✓ Passive Scan (Traffic Analysis)</li>\n");
        html.append("                        <li>✓ OWASP Top 10 Checks</li>\n");
        html.append("                    </ul>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </section>\n");
    }

    private static void generateJsonReport() throws Exception {
        JSONObject report = new JSONObject();
        report.put("timestamp", getCurrentDateTime());
        report.put("scanType", "DAST - Enhanced with Custom Tests");
        report.put("target", "Bosta APIs");

        // APIs
        JSONArray apis = new JSONArray();
        apis.put(createApiObject("Create Pickup", "/api/v2/pickups", "POST"));
        apis.put(createApiObject("Update Bank Info", "/api/v2/businesses/add-bank-info", "POST"));
        apis.put(createApiObject("Forget Password", "/api/v2/users/forget-password", "POST"));
        report.put("apis", apis);

        // Custom Findings
        JSONArray customFindingsArray = new JSONArray();
        for (CustomFinding finding : customFindings) {
            JSONObject f = new JSONObject();
            f.put("issue", finding.issue);
            f.put("severity", finding.severity);
            f.put("testName", finding.testName);
            f.put("api", finding.api);
            f.put("timestamp", finding.timestamp);
            f.put("source", "Custom Test");
            customFindingsArray.put(f);
        }
        report.put("customFindings", customFindingsArray);

        // ZAP Findings
        JSONArray zapFindingsArray = new JSONArray();
        for (VulnerabilityData vuln : getVulnerabilitiesFromZap()) {
            JSONObject finding = new JSONObject();
            finding.put("name", vuln.name);
            finding.put("severity", vuln.risk);
            finding.put("url", vuln.url);
            finding.put("description", vuln.description);
            finding.put("solution", vuln.solution);
            finding.put("source", "OWASP ZAP");
            zapFindingsArray.put(finding);
        }
        report.put("zapFindings", zapFindingsArray);

        // Summary
        JSONObject summary = new JSONObject();
        summary.put("totalFindings", customFindings.size() + getVulnerabilitiesFromZap().size());
        summary.put("customFindings", customFindings.size());
        summary.put("zapFindings", getVulnerabilitiesFromZap().size());
        report.put("summary", summary);

        try (FileWriter writer = new FileWriter(REPORTS_DIR + "/security-report.json")) {
            writer.write(report.toString(2));
        }

        System.out.println("✓ JSON report generated");
    }

    private static void generateMarkdownReport() throws Exception {
        StringBuilder md = new StringBuilder();

        md.append("# 🔒 Enhanced Security Assessment Report\n\n");
        md.append("**Generated:** ").append(getCurrentDateTime()).append("\n\n");

        md.append("## 📊 Executive Summary\n\n");
        md.append("**Total Findings:** ").append(customFindings.size() + getVulnerabilitiesFromZap().size()).append("\n");
        md.append("- Custom Test Findings: ").append(customFindings.size()).append("\n");
        md.append("- OWASP ZAP Findings: ").append(getVulnerabilitiesFromZap().size()).append("\n\n");

        Map<String, Integer> customSeverity = countCustomBySeverity();
        Map<String, Integer> zapSeverity = countBySeverity(getVulnerabilitiesFromZap());

        md.append("### Severity Breakdown\n\n");
        md.append("| Severity | Custom Tests | OWASP ZAP | Total |\n");
        md.append("|----------|-------------|-----------|-------|\n");
        md.append("| High/Critical | ").append(customSeverity.getOrDefault("HIGH", 0) + customSeverity.getOrDefault("CRITICAL", 0))
                .append(" | ").append(zapSeverity.getOrDefault("High", 0))
                .append(" | ").append(customSeverity.getOrDefault("HIGH", 0) + zapSeverity.getOrDefault("High", 0)).append(" |\n");
        md.append("| Medium | ").append(customSeverity.getOrDefault("MEDIUM", 0))
                .append(" | ").append(zapSeverity.getOrDefault("Medium", 0))
                .append(" | ").append(customSeverity.getOrDefault("MEDIUM", 0) + zapSeverity.getOrDefault("Medium", 0)).append(" |\n");
        md.append("| Low | ").append(customSeverity.getOrDefault("LOW", 0))
                .append(" | ").append(zapSeverity.getOrDefault("Low", 0))
                .append(" | ").append(customSeverity.getOrDefault("LOW", 0) + zapSeverity.getOrDefault("Low", 0)).append(" |\n\n");

        md.append("## 🎯 Custom Test Findings\n\n");
        if (customFindings.isEmpty()) {
            md.append("✓ No security issues detected by custom tests!\n\n");
        } else {
            for (CustomFinding finding : customFindings) {
                md.append("### ").append(finding.issue).append("\n\n");
                md.append("- **Severity:** ").append(finding.severity).append("\n");
                md.append("- **API:** ").append(finding.api).append("\n");
                md.append("- **Test:** `").append(finding.testName).append("`\n");
                md.append("- **Detected:** ").append(finding.timestamp).append("\n\n");
                md.append("---\n\n");
            }
        }

        md.append("## 🕷️ OWASP ZAP Findings\n\n");
        List<VulnerabilityData> zapVulns = getVulnerabilitiesFromZap();
        if (zapVulns.isEmpty()) {
            md.append("✓ No vulnerabilities detected by OWASP ZAP!\n\n");
        } else {
            for (VulnerabilityData vuln : zapVulns) {
                md.append("### ").append(vuln.name).append("\n\n");
                md.append("- **Severity:** ").append(vuln.risk).append("\n");
                md.append("- **URL:** `").append(vuln.url).append("`\n");
                md.append("- **Description:** ").append(vuln.description).append("\n");
                if (!vuln.solution.isEmpty()) {
                    md.append("- **Solution:** ").append(vuln.solution).append("\n");
                }
                md.append("\n---\n\n");
            }
        }

        try (FileWriter writer = new FileWriter(REPORTS_DIR + "/security-report.md")) {
            writer.write(md.toString());
        }

        System.out.println("✓ Markdown report generated");
    }

    private static void generateIndexPage() throws Exception {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Security Reports Dashboard</title>\n");
        html.append("    <style>\n");
        html.append(getIndexStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <h1>🔒 Security Assessment Dashboard</h1>\n");
        html.append("        <p class=\"subtitle\">Bosta APIs DAST Pipeline</p>\n");
        html.append("        <div class=\"stats\">\n");
        html.append("            <div class=\"stat\">📊 Total Findings: <strong>")
                .append(customFindings.size() + getVulnerabilitiesFromZap().size()).append("</strong></div>\n");
        html.append("            <div class=\"stat\">🎯 Custom Tests: <strong>")
                .append(customFindings.size()).append("</strong></div>\n");
        html.append("            <div class=\"stat\">🕷️ ZAP Scans: <strong>")
                .append(getVulnerabilitiesFromZap().size()).append("</strong></div>\n");
        html.append("        </div>\n");
        html.append("        <div class=\"cards\">\n");
        html.append("            <a href=\"security-report.html\" class=\"card\">\n");
        html.append("                <h2>📊 Full HTML Report</h2>\n");
        html.append("                <p>Comprehensive security assessment with all findings</p>\n");
        html.append("            </a>\n");
        html.append("            <a href=\"security-report.json\" class=\"card\" download>\n");
        html.append("                <h2>📄 JSON Report</h2>\n");
        html.append("                <p>Machine-readable format for CI/CD integration</p>\n");
        html.append("            </a>\n");
        html.append("            <a href=\"security-report.md\" class=\"card\" download>\n");
        html.append("                <h2>📝 Markdown Report</h2>\n");
        html.append("                <p>Documentation-friendly format</p>\n");
        html.append("            </a>\n");
        html.append("            <a href=\"zap-report.html\" class=\"card\">\n");
        html.append("                <h2>🕷️ ZAP Detailed Report</h2>\n");
        html.append("                <p>OWASP ZAP comprehensive scan results</p>\n");
        html.append("            </a>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");

        try (FileWriter writer = new FileWriter(REPORTS_DIR + "/index.html")) {
            writer.write(html.toString());
        }

        System.out.println("✓ Index page generated");
    }

    // Helper methods
    private static List<VulnerabilityData> getVulnerabilitiesFromZap() {
        List<VulnerabilityData> vulnerabilities = new ArrayList<>();
        try {
            // Use JSON API instead of XML
            String zapHost = System.getProperty("zap.proxy.host", "localhost");
            int zapPort = Integer.parseInt(System.getProperty("zap.proxy.port", "8080"));

            String jsonUrl = "http://" + zapHost + ":" + zapPort + "/JSON/core/view/alerts/";

            // Fetch JSON directly
            URL url = new URL(jsonUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray alerts = jsonResponse.getJSONArray("alerts");

            for (int i = 0; i < alerts.length(); i++) {
                JSONObject alert = alerts.getJSONObject(i);
                VulnerabilityData vuln = new VulnerabilityData();
                vuln.name = alert.optString("alert", "");
                vuln.risk = alert.optString("risk", "");
                vuln.url = alert.optString("url", "");
                vuln.description = alert.optString("description", "");
                vuln.solution = alert.optString("solution", "");
                vuln.reference = alert.optString("reference", "");
                vulnerabilities.add(vuln);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Could not fetch ZAP findings: " + e.getMessage());
        }
        return vulnerabilities;
    }

    private static String getAlertValue(org.zaproxy.clientapi.core.ApiResponseSet alert, String key) {
        try {
            ApiResponse value = alert.getValue(key);
            if (value instanceof ApiResponseElement) {
                return ((ApiResponseElement) value).getValue();
            }
        } catch (Exception e) {
            // Value not found
        }
        return "";
    }

    private static Map<String, Integer> countCustomBySeverity() {
        Map<String, Integer> counts = new HashMap<>();
        for (CustomFinding finding : customFindings) {
            counts.put(finding.severity, counts.getOrDefault(finding.severity, 0) + 1);
        }
        return counts;
    }

    private static Map<String, Integer> countBySeverity(List<VulnerabilityData> vulnerabilities) {
        Map<String, Integer> counts = new HashMap<>();
        for (VulnerabilityData vuln : vulnerabilities) {
            counts.put(vuln.risk, counts.getOrDefault(vuln.risk, 0) + 1);
        }
        return counts;
    }

    private static int countFindingsForApi(String apiName) {
        int count = 0;
        for (CustomFinding finding : customFindings) {
            if (finding.api != null && finding.api.equals(apiName)) {
                count++;
            }
        }
        return count;
    }

    private static String getSeverityClass(String severity) {
        if (severity == null) return "info";
        switch (severity.toUpperCase()) {
            case "CRITICAL":
            case "HIGH":
                return "critical";
            case "MEDIUM":
                return "medium";
            case "LOW":
                return "low";
            default:
                return "info";
        }
    }

    private static JSONObject createApiObject(String name, String endpoint, String method) {
        JSONObject api = new JSONObject();
        api.put("name", name);
        api.put("endpoint", endpoint);
        api.put("method", method);
        return api;
    }

    private static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    private static String getStyles() {
        return "* { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #0f172a; color: #e2e8f0; line-height: 1.6; }\n" +
                "header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 3rem 0; margin-bottom: 2rem; }\n" +
                "header h1 { font-size: 2.5rem; margin-bottom: 0.5rem; }\n" +
                ".subtitle { font-size: 1.2rem; opacity: 0.9; }\n" +
                ".date { font-size: 0.9rem; opacity: 0.8; margin-top: 0.5rem; }\n" +
                ".container { max-width: 1200px; margin: 0 auto; padding: 0 2rem; }\n" +
                "section { background: #1e293b; border-radius: 8px; padding: 2rem; margin-bottom: 2rem; box-shadow: 0 4px 6px rgba(0,0,0,0.3); }\n" +
                "h2 { font-size: 1.8rem; margin-bottom: 1.5rem; color: #f1f5f9; border-bottom: 2px solid #475569; padding-bottom: 0.5rem; }\n" +
                "h3 { font-size: 1.4rem; margin: 1.5rem 0 1rem; color: #cbd5e1; }\n" +
                "h4 { font-size: 1.1rem; margin: 0.5rem 0; color: #f1f5f9; }\n" +
                ".summary-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; }\n" +
                ".stat-card { padding: 1.5rem; border-radius: 8px; text-align: center; }\n" +
                ".stat-card.critical { background: linear-gradient(135deg, #dc2626, #991b1b); }\n" +
                ".stat-card.medium { background: linear-gradient(135deg, #f59e0b, #d97706); }\n" +
                ".stat-card.low { background: linear-gradient(135deg, #eab308, #ca8a04); }\n" +
                ".stat-card.info { background: linear-gradient(135deg, #3b82f6, #2563eb); }\n" +
                ".stat-number { font-size: 3rem; font-weight: bold; }\n" +
                ".stat-label { font-size: 1rem; opacity: 0.9; margin-top: 0.5rem; }\n" +
                ".stat-detail { font-size: 0.85rem; opacity: 0.8; margin-top: 0.5rem; }\n" +
                ".summary-note { margin-top: 1.5rem; padding: 1rem; background: #334155; border-radius: 6px; text-align: center; }\n" +
                ".custom-findings { border-left: 4px solid #f59e0b; }\n" +
                ".findings-summary { background: #334155; padding: 1rem; border-radius: 6px; margin-bottom: 1.5rem; }\n" +
                ".api-findings { margin-bottom: 2rem; }\n" +
                ".info-box { background: #065f46; padding: 1.5rem; border-radius: 6px; border-left: 4px solid #10b981; }\n" +
                ".vulnerability { background: #334155; padding: 1.5rem; border-radius: 6px; margin-bottom: 1.5rem; border-left: 4px solid #64748b; }\n" +
                ".vulnerability.critical { border-left-color: #dc2626; background: linear-gradient(90deg, rgba(220, 38, 38, 0.1) 0%, #334155 10%); }\n" +
                ".vulnerability.medium { border-left-color: #f59e0b; background: linear-gradient(90deg, rgba(245, 158, 11, 0.1) 0%, #334155 10%); }\n" +
                ".vulnerability.low { border-left-color: #eab308; }\n" +
                ".vuln-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; flex-wrap: wrap; gap: 0.5rem; }\n" +
                ".severity-badge { padding: 0.3rem 0.8rem; border-radius: 4px; font-size: 0.85rem; font-weight: bold; text-transform: uppercase; }\n" +
                ".severity-badge.critical { background: #dc2626; }\n" +
                ".severity-badge.medium { background: #f59e0b; }\n" +
                ".severity-badge.low { background: #eab308; color: #000; }\n" +
                ".severity-badge.info { background: #3b82f6; }\n" +
                ".vuln-details p { margin-bottom: 0.8rem; }\n" +
                ".vuln-details code { background: #0f172a; padding: 0.2rem 0.5rem; border-radius: 3px; color: #60a5fa; font-size: 0.9rem; }\n" +
                ".api-list { display: grid; gap: 1rem; }\n" +
                ".api-item { background: #334155; padding: 1.5rem; border-radius: 6px; border-left: 4px solid #667eea; }\n" +
                ".api-item code { background: #0f172a; padding: 0.2rem 0.5rem; border-radius: 3px; color: #60a5fa; }\n" +
                ".api-stats { color: #94a3b8; font-size: 0.9rem; margin-top: 0.5rem; }\n" +
                ".coverage-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 2rem; }\n" +
                ".test-list { list-style: none; padding-left: 0; }\n" +
                ".test-list li { padding: 0.5rem; background: #334155; border-radius: 4px; margin-bottom: 0.5rem; }\n" +
                "footer { background: #0f172a; padding: 2rem 0; margin-top: 3rem; text-align: center; opacity: 0.8; }\n" +
                "code { font-family: 'Courier New', monospace; }";
    }

    private static String getIndexStyles() {
        return "* { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 2rem; }\n" +
                ".container { max-width: 1000px; width: 100%; }\n" +
                "h1 { font-size: 3rem; color: white; text-align: center; margin-bottom: 0.5rem; }\n" +
                ".subtitle { font-size: 1.2rem; color: rgba(255,255,255,0.9); text-align: center; margin-bottom: 2rem; }\n" +
                ".stats { display: flex; justify-content: center; gap: 2rem; margin-bottom: 2rem; flex-wrap: wrap; }\n" +
                ".stat { background: rgba(255,255,255,0.2); padding: 1rem 2rem; border-radius: 8px; color: white; font-size: 1.1rem; }\n" +
                ".cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 1.5rem; }\n" +
                ".card { background: white; padding: 2rem; border-radius: 12px; text-decoration: none; color: #1e293b; box-shadow: 0 10px 30px rgba(0,0,0,0.3); transition: transform 0.3s, box-shadow 0.3s; }\n" +
                ".card:hover { transform: translateY(-5px); box-shadow: 0 15px 40px rgba(0,0,0,0.4); }\n" +
                ".card h2 { font-size: 1.5rem; margin-bottom: 0.5rem; color: #667eea; }\n" +
                ".card p { color: #64748b; font-size: 0.95rem; }";
    }

    // Data classes
    static class VulnerabilityData {
        String name = "";
        String risk = "";
        String url = "";
        String description = "";
        String solution = "";
        String reference = "";
    }

    static class CustomFinding {
        public String issue;
        public String severity;
        public String testName;
        public String api;
        public String timestamp;
    }
}