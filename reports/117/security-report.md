# 🔒 Enhanced Security Assessment Report

**Generated:** 2026-02-15 04:16:07

## 📊 Executive Summary

**Total Findings:** 39
- Custom Test Findings: 12
- OWASP ZAP Findings: 27

### Severity Breakdown

| Severity | Custom Tests | OWASP ZAP | Total |
|----------|-------------|-----------|-------|
| High/Critical | 12 | 0 | 12 |
| Medium | 0 | 13 | 13 |
| Low | 0 | 14 | 14 |

## 🎯 Custom Test Findings

### Bank Info API: Insufficient rate limiting - 100 requests succeeded

- **Severity:** HIGH
- **API:** Update Bank Info API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:39.412291203

---

### Forget Password API: Insufficient rate limiting - 50 requests succeeded

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:50.136075856

---

### Forget Password API: Invalid email format accepted: notanemail

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:52.060339054

---

### Forget Password API: Invalid email format accepted: @test.com

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:52.219901312

---

### Forget Password API: Invalid email format accepted: test@

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:52.379363766

---

### Forget Password API: Invalid email format accepted: test@@test.com

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:52.535161763

---

### Forget Password API: Invalid email format accepted: test@.com

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:52.691214732

---

### Forget Password API: Invalid email format accepted: <script>alert('xss')</script>@test.com

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:52.850090889

---

### Forget Password API: Invalid email format accepted: test@test@test.com

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:53.005880749

---

### Forget Password API: Invalid email format accepted: null

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:53.318876542

---

### Forget Password API: Invalid email format accepted: ../../../etc/passwd

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:53.483387687

---

### Forget Password API: Invalid email format accepted: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA@test.com

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-02-15T04:15:53.642349844

---

## 🕷️ OWASP ZAP Findings

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Content Security Policy (CSP) Header Not Set

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** Content Security Policy (CSP) is an added layer of security that helps to detect and mitigate certain types of attacks, including Cross Site Scripting (XSS) and data injection attacks. These attacks are used for everything from data theft to site defacement or distribution of malware. CSP provides a set of standard HTTP headers that allow website owners to declare approved sources of content that browsers should be allowed to load on that page — covered types are JavaScript, CSS, HTML frames, fonts, images and embeddable objects such as Java applets, ActiveX, audio and video files.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to set the Content-Security-Policy header.

---

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/businesses/add-bank-info`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Content Security Policy (CSP) Header Not Set

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/businesses/add-bank-info`
- **Description:** Content Security Policy (CSP) is an added layer of security that helps to detect and mitigate certain types of attacks, including Cross Site Scripting (XSS) and data injection attacks. These attacks are used for everything from data theft to site defacement or distribution of malware. CSP provides a set of standard HTTP headers that allow website owners to declare approved sources of content that browsers should be allowed to load on that page — covered types are JavaScript, CSS, HTML frames, fonts, images and embeddable objects such as Java applets, ActiveX, audio and video files.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to set the Content-Security-Policy header.

---

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/businesses/add-bank-info`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/businesses/add-bank-info`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/businesses/add-bank-info`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

