package com.htmake.reader.utils;

import com.htmake.reader.entity.MongoFile;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: MongoManager.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/utils/MongoManager.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\t\u001a\u00020\bJ\u001e\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\f2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bJ\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/htmake/reader/utils/MongoManager;", PackageDocumentBase.PREFIX_OPF, "()V", "mongoClient", "Lcom/mongodb/client/MongoClient;", "connect", PackageDocumentBase.PREFIX_OPF, "uri", PackageDocumentBase.PREFIX_OPF, "db", "Lcom/mongodb/client/MongoDatabase;", "fileStorage", "Lcom/mongodb/client/MongoCollection;", "Lcom/htmake/reader/entity/MongoFile;", "collection", "isInit", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public final class MongoManager {

    @NotNull
    public static final MongoManager INSTANCE = new MongoManager();
    private static MongoClient mongoClient;

    private MongoManager() {
    }

    public final boolean isInit() {
        return mongoClient != null;
    }

    public final void connect(@NotNull String uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        try {
            MongoClient mongoClientCreate = MongoClients.create(uri);
            Intrinsics.checkNotNullExpressionValue(mongoClientCreate, "create(uri)");
            mongoClient = mongoClientCreate;
        } catch (MongoException e) {
            ExtKt.getLogger().info("mongodb 连接失败，请检查链接({})是否正确", uri);
            e.printStackTrace();
        }
    }

    @Nullable
    public final MongoDatabase db(@NotNull String db) {
        Intrinsics.checkNotNullParameter(db, "db");
        if (!isInit()) {
            return null;
        }
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(new CodecRegistry[]{MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(new CodecProvider[]{PojoCodecProvider.builder().automatic(true).build()})});
        MongoClient mongoClient2 = mongoClient;
        if (mongoClient2 != null) {
            return mongoClient2.getDatabase(db).withCodecRegistry(pojoCodecRegistry);
        }
        Intrinsics.throwUninitializedPropertyAccessException("mongoClient");
        throw null;
    }

    @Nullable
    public final MongoCollection<MongoFile> fileStorage(@NotNull String db, @NotNull String collection) {
        Intrinsics.checkNotNullParameter(db, "db");
        Intrinsics.checkNotNullParameter(collection, "collection");
        MongoDatabase mongoDatabaseDb = db(db);
        if (mongoDatabaseDb == null) {
            return null;
        }
        return mongoDatabaseDb.getCollection(collection, MongoFile.class);
    }
}
