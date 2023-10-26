package com.graph_proj.graph.models.requests;

public record CalculCheminRequest(String source,String destination) {


    public boolean isInvalid(){
        return source == null || source.isEmpty() || destination == null || destination.isEmpty();
    }
}
