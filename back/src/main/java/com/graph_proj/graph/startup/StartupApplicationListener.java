package com.graph_proj.graph.startup;

import com.graph_proj.graph.global.Constants;
import com.graph_proj.graph.services.graphe.GrapheInitializr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(StartupApplicationListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try{
            GrapheInitializr.importer(Constants.METRO,Constants.fileName);
        }catch (Exception e){
            logger.error("Failed to initialize metro " + e);
        }
    }
}