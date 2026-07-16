// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import com.htmake.reader.entity.MongoFile;
import com.mongodb.client.MongoCollection;
import org.jetbrains.annotations.Nullable;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecProvider;
import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClients;
import kotlin.jvm.internal.Intrinsics;
import com.mongodb.client.MongoClient;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\t\u001a\u00020\bJ\u001e\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\f2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bJ\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.?\u0006\u0002\n\u0000¡§\u0006\u0011" }, d2 = { "Lcom/htmake/reader/utils/MongoManager;", "", "()V", "mongoClient", "Lcom/mongodb/client/MongoClient;", "connect", "", "uri", "", "db", "Lcom/mongodb/client/MongoDatabase;", "fileStorage", "Lcom/mongodb/client/MongoCollection;", "Lcom/htmake/reader/entity/MongoFile;", "collection", "isInit", "", "reader-pro" })
public final class MongoManager
{
    @NotNull
    public static final MongoManager INSTANCE;
    private static MongoClient mongoClient;
    
    private MongoManager() {
    }
    
    public final boolean isInit() {
        return MongoManager.mongoClient != null;
    }
    
    public final void connect(@NotNull final String uri) {
        Intrinsics.checkNotNullParameter((Object)uri, "uri");
        try {
            final MongoClient create = MongoClients.create(uri);
            Intrinsics.checkNotNullExpressionValue((Object)create, "create(uri)");
            MongoManager.mongoClient = create;
        }
        catch (final MongoException e) {
            ExtKt.getLogger().info("mongodb \u8fde\u63a5\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u94fe\u63a5({})\u662f\u5426\u6b63\u786e", (Object)uri);
            e.printStackTrace();
        }
    }
    
    @Nullable
    public final MongoDatabase db(@NotNull final String db) {
        Intrinsics.checkNotNullParameter((Object)db, "db");
        if (!this.isInit()) {
            return null;
        }
        final PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(new CodecRegistry[] { MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(new CodecProvider[] { (CodecProvider)pojoCodecProvider }) });
        final MongoClient mongoClient = MongoManager.mongoClient;
        if (mongoClient == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mongoClient");
            throw null;
        }
        return mongoClient.getDatabase(db).withCodecRegistry(pojoCodecRegistry);
    }
    
    @Nullable
    public final MongoCollection<MongoFile> fileStorage(@NotNull final String db, @NotNull final String collection) {
        Intrinsics.checkNotNullParameter((Object)db, "db");
        Intrinsics.checkNotNullParameter((Object)collection, "collection");
        final MongoDatabase db2 = this.db(db);
        return (MongoCollection<MongoFile>)((db2 == null) ? null : db2.getCollection(collection, (Class)MongoFile.class));
    }
    
    static {
        INSTANCE = new MongoManager();
    }
}
