package com.htmake.reader.utils

import com.htmake.reader.entity.MongoFile
import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kotlin.jvm.internal.Intrinsics
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider

public object MongoManager {
   private final lateinit var mongoClient: MongoClient

   public fun isInit(): Boolean {
      return mongoClient != null;
   }

   public fun connect(uri: String) {
      try {
         val e: MongoClient = MongoClients.create(uri);
         mongoClient = e;
      } catch (var3: MongoException) {
         ExtKt.getLogger().info("mongodb 连接失败，请检查链接({})是否正确", uri);
         var3.printStackTrace();
      }
   }

   public fun db(db: String): MongoDatabase? {
      if (!this.isInit()) {
         return null;
      } else {
         val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
         );
         if (mongoClient == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mongoClient");
            throw null;
         } else {
            return mongoClient.getDatabase(db).withCodecRegistry(pojoCodecRegistry);
         }
      }
   }

   public fun fileStorage(db: String, collection: String): MongoCollection<MongoFile>? {
      val var3: MongoDatabase = this.db(db);
      return if (var3 == null) null else var3.getCollection(collection, MongoFile.class);
   }
}
