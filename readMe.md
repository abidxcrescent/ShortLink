# URL Shortening Service

This is a simple **URL shortening service** that allows users to shorten long URLs and expand them back to their original form. It is implemented in **Java** using **Spring Boot** and offers basic features such as concurrency control, input validation, and the ability to handle requests only from the local machine.

## Features

- **URL Shortening**: Converts long URLs into shorter versions.
- **URL Expansion**: Expands a shortened URL back to its original form.
- **Concurrency **: Limits the number of concurrent requests to prevent server overload.
- **Localhost-only Access**: The service is designed to only accept requests from the local machine (127.0.0.1 or ::1).
- **Basic Security**: Includes simple measures to prevent access from external machines and block potentially harmful URLs.

## Getting Started

These instructions will help you set up and run the URL shortening service on your local machine.

### Prerequisites

To run this project, you need the following software:

- Java
- Maven
- Spring Boot