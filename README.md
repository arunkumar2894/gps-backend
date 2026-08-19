GPS Backend Service

Endpoints:
- GET /secrets
  Returns the in-memory secrets JSON.

- PUT /secrets/{token}/{api}
  Updates a single secret. Path variables:
    token - must match app.update.token (default: secret-token-123)
    api   - "mapsApiKey" or "deviceUuid"
  Body JSON: { "value": "new-value" }

Example:
  curl http://localhost:8080/secrets
  curl -X PUT -H "Content-Type: application/json" -d '{"value":"NEW"}' http://localhost:8080/secrets/secret-token-123/mapsApiKey
