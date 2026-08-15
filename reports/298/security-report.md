# 🔒 Enhanced Security Assessment Report

**Generated:** 2026-08-15 02:47:21

## 📊 Executive Summary

**Total Findings:** 37
- Custom Test Findings: 2
- OWASP ZAP Findings: 35

### Severity Breakdown

| Severity | Custom Tests | OWASP ZAP | Total |
|----------|-------------|-----------|-------|
| High/Critical | 2 | 0 | 2 |
| Medium | 0 | 11 | 11 |
| Low | 0 | 24 | 24 |

## 🎯 Custom Test Findings

### Bank Info API: Insufficient rate limiting - 100 requests succeeded

- **Severity:** HIGH
- **API:** Update Bank Info API
- **Test:** `unknown`
- **Detected:** 2026-08-15T02:46:54.368251474

---

### Forget Password API: Doesn't work without auth - this breaks the user flow!

- **Severity:** HIGH
- **API:** Forget Password API
- **Test:** `unknown`
- **Detected:** 2026-08-15T02:47:13.624235869

---

## 🕷️ OWASP ZAP Findings

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

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

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

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

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

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

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

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

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/pickups`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

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

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/businesses/add-bank-info`
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

### Cross-Domain Misconfiguration

- **Severity:** Medium
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** Web browser data loading may be possible, due to a Cross Origin Resource Sharing (CORS) misconfiguration on the web server.
- **Solution:** Ensure that sensitive data is not available in an unauthenticated manner (using IP address white-listing, for instance).
Configure the "Access-Control-Allow-Origin" HTTP header to a more restrictive set of domains, or remove all CORS headers entirely, to allow the web browser to enforce the Same Origin Policy (SOP) in a more restrictive manner.

---

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

---

### Server Leaks Information via "X-Powered-By" HTTP Response Header Field(s)

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** The web/application server is leaking information via one or more "X-Powered-By" HTTP response headers. Access to such information may facilitate attackers identifying other frameworks/components your web application is reliant upon and the vulnerabilities such components may be subject to.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to suppress "X-Powered-By" headers.

---

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

---

### Strict-Transport-Security Header Not Set

- **Severity:** Low
- **URL:** `https://stg-app.bosta.co/api/v2/users/forget-password`
- **Description:** HTTP Strict Transport Security (HSTS) is a web security policy mechanism whereby a web server declares that complying user agents (such as a web browser) are to interact with it using only secure HTTPS connections (i.e. HTTP layered over TLS/SSL). HSTS is an IETF standards track protocol and is specified in RFC 6797.
- **Solution:** Ensure that your web server, application server, load balancer, etc. is configured to enforce Strict-Transport-Security.

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

