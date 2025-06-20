# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## [1.0.0] 2025-05-30
### Added
- Improved REST API : Now able to generate captions for image content types [PNG, GIF, JPG, JPEG]
- Added MIME type detection to handle various image formats
- Improved Error Handling in REST API : Backend API now responds with Unsupported Media Type | Media Size too large | Empty file errors
- Structured Error response

## [1.0.0] 2025-05-31
### Added
- API Request Rate Limiting : Added API Rate Limiting (Token Bucket Rate Limiting)

## [1.0.0] 2025-06-01
### Changed
- Switched to my own implementation of Rate Limiter (Fixed API Rate Limiting)

## [1.0.0] 2025-06-03
### Added
- Added test for the REST API using Ollama test containers

## [1.0.0] 2025-06-04
### Changed
- Refactored the REST API endpoint URI to adhere to REST Principles / Best Practices

## [1.0.0] 2025-06-05
### Added
- Saving the uploaded image files on local Disk

## [1.0.0] 2025-06-06
### Added
- Added Basic Auth to /captions REST API
- Added RBAC(Role-Based-Access-Control) to the RESTP API

## [1.0.0] 2025-06-07
### Added
- Redis connection settings made configurable. Now, connect to any remote instance as well

### Changed
- Switched to JWT Authentication from Basic Auth

## [1.0.0] 2025-06-08

### Added
- Uploading the image files on AWS S3 autocaption-images bucket

## [1.0.0] 2025-06-14

### Added
- Allowed Configuring rate limiting algorithms. Default is fixed window rate limiter. Choose among - FixedWindow, SlidingWindow, TockenBucket

## [1.0.0] 2025-06-17

### Added
- Added refresh token end point to refresh expired access tokens using long-lived refresh tokens

## [1.0.0] 2025-06-18

### Changed
- Switched from plain Vanilla JS to Angular 19