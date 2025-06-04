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