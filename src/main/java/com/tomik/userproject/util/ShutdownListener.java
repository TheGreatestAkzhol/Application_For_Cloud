//package com.tomik.userproject.util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextClosedEvent;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public void onApplicationEvent(ContextClosedEvent event) {
//        cleanup();
//    }
//
//    private void cleanup() {
//        for (String collectionName : mongoTemplate.getCollectionNames()) {
//            mongoTemplate.dropCollection(collectionName);
//        }
//    }
//}
