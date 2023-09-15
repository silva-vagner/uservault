package com.uservault.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nullable;

public class AuditDetailDTO {
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode previous;
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode following;
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    AuditDetailDTO(){}

    public AuditDetailDTO(JsonNode previous, JsonNode following, String message) {
        this.previous = previous;
        this.following = following;
        this.message = message;
    }

    public JsonNode getPrevious() {
        return previous;
    }

    public void setPrevious(JsonNode previous) {
        this.previous = previous;
    }

    public JsonNode getFollowing() {
        return following;
    }

    public void setFollowing(JsonNode following) {
        this.following = following;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
