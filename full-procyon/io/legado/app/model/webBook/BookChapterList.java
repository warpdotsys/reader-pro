// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.webBook;

import kotlin.Pair;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.data.entities.BookChapter;
import java.util.List;
import kotlin.coroutines.Continuation;
import io.legado.app.model.DebugLog;
import io.legado.app.data.entities.BookSource;
import org.jetbrains.annotations.Nullable;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0081\u0001\u0010\u0003\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00050\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00132\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0017JM\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\b\u001a\u00020\t2\b\u0010\f\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u0019" }, d2 = { "Lio/legado/app/model/webBook/BookChapterList;", "", "()V", "analyzeChapterList", "Lkotlin/Pair;", "", "Lio/legado/app/data/entities/BookChapter;", "", "book", "Lio/legado/app/data/entities/Book;", "baseUrl", "redirectUrl", "body", "tocRule", "Lio/legado/app/data/entities/rule/TocRule;", "listRule", "bookSource", "Lio/legado/app/data/entities/BookSource;", "getNextUrl", "", "log", "debugLog", "Lio/legado/app/model/DebugLog;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/rule/TocRule;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;ZZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class BookChapterList
{
    @NotNull
    public static final BookChapterList INSTANCE;
    
    private BookChapterList() {
    }
    
    @Nullable
    public final Object analyzeChapterList(@NotNull final Book book, @Nullable final String body, @NotNull final BookSource bookSource, @NotNull final String baseUrl, @NotNull final String redirectUrl, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super List<BookChapter>> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: instanceof      Lio/legado/app/model/webBook/BookChapterList$analyzeChapterList$1;
        //     5: ifeq            41
        //     8: aload           7
        //    10: checkcast       Lio/legado/app/model/webBook/BookChapterList$analyzeChapterList$1;
        //    13: astore          29
        //    15: aload           29
        //    17: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //    20: ldc             -2147483648
        //    22: iand           
        //    23: ifeq            41
        //    26: aload           29
        //    28: dup            
        //    29: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //    32: ldc             -2147483648
        //    34: isub           
        //    35: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //    38: goto            53
        //    41: new             Lio/legado/app/model/webBook/BookChapterList$analyzeChapterList$1;
        //    44: dup            
        //    45: aload_0        
        //    46: aload           7
        //    48: invokespecial   io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.<init>:(Lio/legado/app/model/webBook/BookChapterList;Lkotlin/coroutines/Continuation;)V
        //    51: astore          $continuation
        //    53: aload           $continuation
        //    55: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.result:Ljava/lang/Object;
        //    58: astore          $result
        //    60: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    63: astore          30
        //    65: aload           $continuation
        //    67: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //    70: tableswitch {
        //                0: 104
        //                1: 499
        //                2: 919
        //                3: 1191
        //                4: 1593
        //          default: 1940
        //        }
        //   104: aload           $result
        //   106: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   109: aload_2         /* body */
        //   110: astore          8
        //   112: aload           8
        //   114: ifnonnull       127
        //   117: new             Ljava/lang/Exception;
        //   120: dup            
        //   121: ldc             "error_get_web_content"
        //   123: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   126: athrow         
        //   127: aload           8
        //   129: pop            
        //   130: iconst_0       
        //   131: istore          9
        //   133: new             Ljava/util/ArrayList;
        //   136: dup            
        //   137: invokespecial   java/util/ArrayList.<init>:()V
        //   140: astore          chapterList
        //   142: aload           debugLog
        //   144: astore          9
        //   146: aload           9
        //   148: ifnonnull       155
        //   151: aconst_null    
        //   152: goto            177
        //   155: aload           9
        //   157: aload_3         /* bookSource */
        //   158: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   161: ldc             "\u2261\u83b7\u53d6\u6210\u529f:"
        //   163: aload           baseUrl
        //   165: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   168: iconst_0       
        //   169: iconst_4       
        //   170: aconst_null    
        //   171: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   174: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   177: pop            
        //   178: aload_3         /* bookSource */
        //   179: invokevirtual   io/legado/app/data/entities/BookSource.getTocRule:()Lio/legado/app/data/entities/rule/TocRule;
        //   182: astore          tocRule
        //   184: iconst_1       
        //   185: anewarray       Ljava/lang/String;
        //   188: astore          11
        //   190: aload           11
        //   192: iconst_0       
        //   193: aload           redirectUrl
        //   195: aastore        
        //   196: aload           11
        //   198: invokestatic    kotlin/collections/CollectionsKt.arrayListOf:([Ljava/lang/Object;)Ljava/util/ArrayList;
        //   201: astore          nextUrlList
        //   203: iconst_0       
        //   204: istore          reverse
        //   206: new             Lkotlin/jvm/internal/Ref$ObjectRef;
        //   209: dup            
        //   210: invokespecial   kotlin/jvm/internal/Ref$ObjectRef.<init>:()V
        //   213: astore          listRule
        //   215: aload           listRule
        //   217: aload           tocRule
        //   219: invokevirtual   io/legado/app/data/entities/rule/TocRule.getChapterList:()Ljava/lang/String;
        //   222: astore          13
        //   224: aload           13
        //   226: ifnonnull       234
        //   229: ldc             ""
        //   231: goto            236
        //   234: aload           13
        //   236: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   239: aload           listRule
        //   241: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   244: checkcast       Ljava/lang/String;
        //   247: ldc             "-"
        //   249: iconst_0       
        //   250: iconst_2       
        //   251: aconst_null    
        //   252: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //   255: ifeq            309
        //   258: iconst_1       
        //   259: istore          reverse
        //   261: aload           listRule
        //   263: aload           listRule
        //   265: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   268: checkcast       Ljava/lang/String;
        //   271: astore          13
        //   273: iconst_1       
        //   274: istore          14
        //   276: iconst_0       
        //   277: istore          15
        //   279: aload           13
        //   281: dup            
        //   282: ifnonnull       295
        //   285: new             Ljava/lang/NullPointerException;
        //   288: dup            
        //   289: ldc             "null cannot be cast to non-null type java.lang.String"
        //   291: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   294: athrow         
        //   295: iload           14
        //   297: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   300: dup            
        //   301: ldc             "(this as java.lang.String).substring(startIndex)"
        //   303: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   306: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   309: aload           listRule
        //   311: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   314: checkcast       Ljava/lang/String;
        //   317: ldc             "+"
        //   319: iconst_0       
        //   320: iconst_2       
        //   321: aconst_null    
        //   322: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //   325: ifeq            376
        //   328: aload           listRule
        //   330: aload           listRule
        //   332: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   335: checkcast       Ljava/lang/String;
        //   338: astore          13
        //   340: iconst_1       
        //   341: istore          14
        //   343: iconst_0       
        //   344: istore          15
        //   346: aload           13
        //   348: dup            
        //   349: ifnonnull       362
        //   352: new             Ljava/lang/NullPointerException;
        //   355: dup            
        //   356: ldc             "null cannot be cast to non-null type java.lang.String"
        //   358: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   361: athrow         
        //   362: iload           14
        //   364: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   367: dup            
        //   368: ldc             "(this as java.lang.String).substring(startIndex)"
        //   370: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   373: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   376: new             Lkotlin/jvm/internal/Ref$ObjectRef;
        //   379: dup            
        //   380: invokespecial   kotlin/jvm/internal/Ref$ObjectRef.<init>:()V
        //   383: astore          chapterData
        //   385: aload           chapterData
        //   387: astore          26
        //   389: aload_0         /* this */
        //   390: aload_1         /* book */
        //   391: aload           baseUrl
        //   393: aload           redirectUrl
        //   395: aload_2         /* body */
        //   396: aload           tocRule
        //   398: aload           listRule
        //   400: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   403: checkcast       Ljava/lang/String;
        //   406: aload_3         /* bookSource */
        //   407: iconst_1       
        //   408: iconst_1       
        //   409: aload           debugLog
        //   411: aload           $continuation
        //   413: aload           $continuation
        //   415: aload_1         /* book */
        //   416: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //   419: aload           $continuation
        //   421: aload_3         /* bookSource */
        //   422: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //   425: aload           $continuation
        //   427: aload           debugLog
        //   429: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //   432: aload           $continuation
        //   434: aload           chapterList
        //   436: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$3:Ljava/lang/Object;
        //   439: aload           $continuation
        //   441: aload           tocRule
        //   443: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$4:Ljava/lang/Object;
        //   446: aload           $continuation
        //   448: aload           nextUrlList
        //   450: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$5:Ljava/lang/Object;
        //   453: aload           $continuation
        //   455: aload           listRule
        //   457: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$6:Ljava/lang/Object;
        //   460: aload           $continuation
        //   462: aload           chapterData
        //   464: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$7:Ljava/lang/Object;
        //   467: aload           $continuation
        //   469: aload           26
        //   471: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$8:Ljava/lang/Object;
        //   474: aload           $continuation
        //   476: iload           reverse
        //   478: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //   481: aload           $continuation
        //   483: iconst_1       
        //   484: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //   487: invokespecial   io/legado/app/model/webBook/BookChapterList.analyzeChapterList:(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/rule/TocRule;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;ZZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   490: dup            
        //   491: aload           30
        //   493: if_acmpne       601
        //   496: aload           30
        //   498: areturn        
        //   499: aload           $continuation
        //   501: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //   504: istore          11
        //   506: aload           $continuation
        //   508: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$8:Ljava/lang/Object;
        //   511: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   514: astore          26
        //   516: aload           $continuation
        //   518: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$7:Ljava/lang/Object;
        //   521: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   524: astore          chapterData
        //   526: aload           $continuation
        //   528: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$6:Ljava/lang/Object;
        //   531: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   534: astore          12
        //   536: aload           $continuation
        //   538: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$5:Ljava/lang/Object;
        //   541: checkcast       Ljava/util/ArrayList;
        //   544: astore          10
        //   546: aload           $continuation
        //   548: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$4:Ljava/lang/Object;
        //   551: checkcast       Lio/legado/app/data/entities/rule/TocRule;
        //   554: astore          9
        //   556: aload           $continuation
        //   558: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$3:Ljava/lang/Object;
        //   561: checkcast       Ljava/util/ArrayList;
        //   564: astore          8
        //   566: aload           $continuation
        //   568: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //   571: checkcast       Lio/legado/app/model/DebugLog;
        //   574: astore          6
        //   576: aload           $continuation
        //   578: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //   581: checkcast       Lio/legado/app/data/entities/BookSource;
        //   584: astore_3       
        //   585: aload           $continuation
        //   587: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //   590: checkcast       Lio/legado/app/data/entities/Book;
        //   593: astore_1       
        //   594: aload           $result
        //   596: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   599: aload           $result
        //   601: astore          27
        //   603: aload           26
        //   605: aload           27
        //   607: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   610: aload           8
        //   612: aload           chapterData
        //   614: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   617: checkcast       Lkotlin/Pair;
        //   620: invokevirtual   kotlin/Pair.getFirst:()Ljava/lang/Object;
        //   623: checkcast       Ljava/util/Collection;
        //   626: invokevirtual   java/util/ArrayList.addAll:(Ljava/util/Collection;)Z
        //   629: pop            
        //   630: aload           chapterData
        //   632: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   635: checkcast       Lkotlin/Pair;
        //   638: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   641: checkcast       Ljava/util/List;
        //   644: invokeinterface java/util/List.size:()I
        //   649: istore          14
        //   651: iload           14
        //   653: tableswitch {
        //                0: 676
        //                1: 682
        //          default: 1424
        //        }
        //   676: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   679: goto            1640
        //   682: new             Lkotlin/jvm/internal/Ref$ObjectRef;
        //   685: dup            
        //   686: invokespecial   kotlin/jvm/internal/Ref$ObjectRef.<init>:()V
        //   689: astore          nextUrl
        //   691: aload           nextUrl
        //   693: aload           chapterData
        //   695: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   698: checkcast       Lkotlin/Pair;
        //   701: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   704: checkcast       Ljava/util/List;
        //   707: iconst_0       
        //   708: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   713: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   716: aload           nextUrl
        //   718: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   721: checkcast       Ljava/lang/CharSequence;
        //   724: astore          16
        //   726: iconst_0       
        //   727: istore          17
        //   729: aload           16
        //   731: invokeinterface java/lang/CharSequence.length:()I
        //   736: ifle            743
        //   739: iconst_1       
        //   740: goto            744
        //   743: iconst_0       
        //   744: ifeq            1380
        //   747: aload           10
        //   749: aload           nextUrl
        //   751: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   754: invokevirtual   java/util/ArrayList.contains:(Ljava/lang/Object;)Z
        //   757: ifne            1380
        //   760: aload           10
        //   762: aload           nextUrl
        //   764: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   767: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   770: pop            
        //   771: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl;
        //   774: dup            
        //   775: aload           nextUrl
        //   777: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   780: checkcast       Ljava/lang/String;
        //   783: aconst_null    
        //   784: aconst_null    
        //   785: aconst_null    
        //   786: aconst_null    
        //   787: aconst_null    
        //   788: aload_3        
        //   789: checkcast       Lio/legado/app/data/entities/BaseSource;
        //   792: aload_1        
        //   793: checkcast       Lio/legado/app/model/analyzeRule/RuleDataInterface;
        //   796: aconst_null    
        //   797: aload_3        
        //   798: checkcast       Lio/legado/app/data/entities/BaseSource;
        //   801: iconst_0       
        //   802: iconst_1       
        //   803: aconst_null    
        //   804: invokestatic    io/legado/app/data/entities/BaseSource$DefaultImpls.getHeaderMap$default:(Lio/legado/app/data/entities/BaseSource;ZILjava/lang/Object;)Ljava/util/HashMap;
        //   807: checkcast       Ljava/util/Map;
        //   810: aload           6
        //   812: sipush          318
        //   815: aconst_null    
        //   816: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   819: aconst_null    
        //   820: aconst_null    
        //   821: iconst_0       
        //   822: aload           $continuation
        //   824: bipush          7
        //   826: aconst_null    
        //   827: aload           $continuation
        //   829: aload_1        
        //   830: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //   833: aload           $continuation
        //   835: aload_3        
        //   836: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //   839: aload           $continuation
        //   841: aload           6
        //   843: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //   846: aload           $continuation
        //   848: aload           8
        //   850: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$3:Ljava/lang/Object;
        //   853: aload           $continuation
        //   855: aload           9
        //   857: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$4:Ljava/lang/Object;
        //   860: aload           $continuation
        //   862: aload           10
        //   864: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$5:Ljava/lang/Object;
        //   867: aload           $continuation
        //   869: aload           12
        //   871: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$6:Ljava/lang/Object;
        //   874: aload           $continuation
        //   876: aload           chapterData
        //   878: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$7:Ljava/lang/Object;
        //   881: aload           $continuation
        //   883: aload           nextUrl
        //   885: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$8:Ljava/lang/Object;
        //   888: aload           $continuation
        //   890: aconst_null    
        //   891: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$9:Ljava/lang/Object;
        //   894: aload           $continuation
        //   896: iload           11
        //   898: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //   901: aload           $continuation
        //   903: iconst_2       
        //   904: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //   907: invokestatic    io/legado/app/model/analyzeRule/AnalyzeUrl.getStrResponseAwait$default:(Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   910: dup            
        //   911: aload           30
        //   913: if_acmpne       1021
        //   916: aload           30
        //   918: areturn        
        //   919: aload           $continuation
        //   921: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //   924: istore          11
        //   926: aload           $continuation
        //   928: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$8:Ljava/lang/Object;
        //   931: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   934: astore          15
        //   936: aload           $continuation
        //   938: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$7:Ljava/lang/Object;
        //   941: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   944: astore          13
        //   946: aload           $continuation
        //   948: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$6:Ljava/lang/Object;
        //   951: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   954: astore          12
        //   956: aload           $continuation
        //   958: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$5:Ljava/lang/Object;
        //   961: checkcast       Ljava/util/ArrayList;
        //   964: astore          10
        //   966: aload           $continuation
        //   968: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$4:Ljava/lang/Object;
        //   971: checkcast       Lio/legado/app/data/entities/rule/TocRule;
        //   974: astore          9
        //   976: aload           $continuation
        //   978: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$3:Ljava/lang/Object;
        //   981: checkcast       Ljava/util/ArrayList;
        //   984: astore          8
        //   986: aload           $continuation
        //   988: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //   991: checkcast       Lio/legado/app/model/DebugLog;
        //   994: astore          6
        //   996: aload           $continuation
        //   998: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //  1001: checkcast       Lio/legado/app/data/entities/BookSource;
        //  1004: astore_3       
        //  1005: aload           $continuation
        //  1007: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //  1010: checkcast       Lio/legado/app/data/entities/Book;
        //  1013: astore_1       
        //  1014: aload           $result
        //  1016: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1019: aload           $result
        //  1021: checkcast       Lio/legado/app/help/http/StrResponse;
        //  1024: invokevirtual   io/legado/app/help/http/StrResponse.getBody:()Ljava/lang/String;
        //  1027: astore          16
        //  1029: aload           16
        //  1031: ifnonnull       1038
        //  1034: aconst_null    
        //  1035: goto            1376
        //  1038: aload           16
        //  1040: astore          17
        //  1042: iconst_0       
        //  1043: istore          18
        //  1045: iconst_0       
        //  1046: istore          19
        //  1048: aload           17
        //  1050: astore          nextBody
        //  1052: iconst_0       
        //  1053: istore          $i$a$-let-BookChapterList$analyzeChapterList$2
        //  1055: aload           13
        //  1057: astore          22
        //  1059: getstatic       io/legado/app/model/webBook/BookChapterList.INSTANCE:Lio/legado/app/model/webBook/BookChapterList;
        //  1062: aload_1        
        //  1063: aload           15
        //  1065: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1068: checkcast       Ljava/lang/String;
        //  1071: aload           15
        //  1073: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1076: checkcast       Ljava/lang/String;
        //  1079: aload           nextBody
        //  1081: aload           9
        //  1083: aload           12
        //  1085: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1088: checkcast       Ljava/lang/String;
        //  1091: aload_3        
        //  1092: iconst_1       
        //  1093: iconst_0       
        //  1094: aload           6
        //  1096: aload           $continuation
        //  1098: aload           $continuation
        //  1100: aload_1        
        //  1101: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //  1104: aload           $continuation
        //  1106: aload_3        
        //  1107: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //  1110: aload           $continuation
        //  1112: aload           6
        //  1114: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //  1117: aload           $continuation
        //  1119: aload           8
        //  1121: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$3:Ljava/lang/Object;
        //  1124: aload           $continuation
        //  1126: aload           9
        //  1128: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$4:Ljava/lang/Object;
        //  1131: aload           $continuation
        //  1133: aload           10
        //  1135: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$5:Ljava/lang/Object;
        //  1138: aload           $continuation
        //  1140: aload           12
        //  1142: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$6:Ljava/lang/Object;
        //  1145: aload           $continuation
        //  1147: aload           13
        //  1149: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$7:Ljava/lang/Object;
        //  1152: aload           $continuation
        //  1154: aload           15
        //  1156: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$8:Ljava/lang/Object;
        //  1159: aload           $continuation
        //  1161: aload           22
        //  1163: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$9:Ljava/lang/Object;
        //  1166: aload           $continuation
        //  1168: iload           11
        //  1170: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //  1173: aload           $continuation
        //  1175: iconst_3       
        //  1176: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //  1179: invokespecial   io/legado/app/model/webBook/BookChapterList.analyzeChapterList:(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/rule/TocRule;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;ZZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1182: dup            
        //  1183: aload           30
        //  1185: if_acmpne       1306
        //  1188: aload           30
        //  1190: areturn        
        //  1191: iconst_0       
        //  1192: istore          $i$a$-let-BookChapterList$analyzeChapterList$2
        //  1194: aload           $continuation
        //  1196: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //  1199: istore          11
        //  1201: aload           $continuation
        //  1203: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$9:Ljava/lang/Object;
        //  1206: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //  1209: astore          22
        //  1211: aload           $continuation
        //  1213: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$8:Ljava/lang/Object;
        //  1216: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //  1219: astore          15
        //  1221: aload           $continuation
        //  1223: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$7:Ljava/lang/Object;
        //  1226: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //  1229: astore          13
        //  1231: aload           $continuation
        //  1233: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$6:Ljava/lang/Object;
        //  1236: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //  1239: astore          12
        //  1241: aload           $continuation
        //  1243: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$5:Ljava/lang/Object;
        //  1246: checkcast       Ljava/util/ArrayList;
        //  1249: astore          10
        //  1251: aload           $continuation
        //  1253: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$4:Ljava/lang/Object;
        //  1256: checkcast       Lio/legado/app/data/entities/rule/TocRule;
        //  1259: astore          9
        //  1261: aload           $continuation
        //  1263: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$3:Ljava/lang/Object;
        //  1266: checkcast       Ljava/util/ArrayList;
        //  1269: astore          8
        //  1271: aload           $continuation
        //  1273: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //  1276: checkcast       Lio/legado/app/model/DebugLog;
        //  1279: astore          6
        //  1281: aload           $continuation
        //  1283: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //  1286: checkcast       Lio/legado/app/data/entities/BookSource;
        //  1289: astore_3       
        //  1290: aload           $continuation
        //  1292: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //  1295: checkcast       Lio/legado/app/data/entities/Book;
        //  1298: astore_1       
        //  1299: aload           $result
        //  1301: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1304: aload           $result
        //  1306: astore          23
        //  1308: aload           22
        //  1310: aload           23
        //  1312: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1315: aload           15
        //  1317: aload           13
        //  1319: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1322: checkcast       Lkotlin/Pair;
        //  1325: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //  1328: checkcast       Ljava/util/List;
        //  1331: invokestatic    kotlin/collections/CollectionsKt.firstOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //  1334: checkcast       Ljava/lang/String;
        //  1337: astore          24
        //  1339: aload           24
        //  1341: ifnonnull       1349
        //  1344: ldc             ""
        //  1346: goto            1351
        //  1349: aload           24
        //  1351: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1354: aload           8
        //  1356: aload           13
        //  1358: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1361: checkcast       Lkotlin/Pair;
        //  1364: invokevirtual   kotlin/Pair.getFirst:()Ljava/lang/Object;
        //  1367: checkcast       Ljava/util/Collection;
        //  1370: invokevirtual   java/util/ArrayList.addAll:(Ljava/util/Collection;)Z
        //  1373: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxBoolean:(Z)Ljava/lang/Boolean;
        //  1376: pop            
        //  1377: goto            716
        //  1380: aload           6
        //  1382: astore          16
        //  1384: aload           16
        //  1386: ifnonnull       1393
        //  1389: aconst_null    
        //  1390: goto            1640
        //  1393: aload           16
        //  1395: aload_3        
        //  1396: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1399: ldc             "\u25c7\u76ee\u5f55\u603b\u9875\u6570:"
        //  1401: aload           10
        //  1403: invokevirtual   java/util/ArrayList.size:()I
        //  1406: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //  1409: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1412: iconst_0       
        //  1413: iconst_4       
        //  1414: aconst_null    
        //  1415: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1418: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1421: goto            1640
        //  1424: aload           6
        //  1426: astore          15
        //  1428: aload           15
        //  1430: ifnonnull       1437
        //  1433: aconst_null    
        //  1434: goto            1480
        //  1437: aload           15
        //  1439: aload_3        
        //  1440: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1443: ldc_w           "\u25c7\u5e76\u53d1\u89e3\u6790\u76ee\u5f55,\u603b\u9875\u6570:"
        //  1446: aload           13
        //  1448: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1451: checkcast       Lkotlin/Pair;
        //  1454: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //  1457: checkcast       Ljava/util/List;
        //  1460: invokeinterface java/util/List.size:()I
        //  1465: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //  1468: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1471: iconst_0       
        //  1472: iconst_4       
        //  1473: aconst_null    
        //  1474: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1477: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1480: pop            
        //  1481: invokestatic    kotlinx/coroutines/Dispatchers.getIO:()Lkotlinx/coroutines/CoroutineDispatcher;
        //  1484: checkcast       Lkotlin/coroutines/CoroutineContext;
        //  1487: new             Lio/legado/app/model/webBook/BookChapterList$analyzeChapterList$3;
        //  1490: dup            
        //  1491: aload           13
        //  1493: aload_3        
        //  1494: aload_1        
        //  1495: aload           6
        //  1497: aload           9
        //  1499: aload           12
        //  1501: aload           8
        //  1503: aconst_null    
        //  1504: invokespecial   io/legado/app/model/webBook/BookChapterList$analyzeChapterList$3.<init>:(Lkotlin/jvm/internal/Ref$ObjectRef;Lio/legado/app/data/entities/BookSource;Lio/legado/app/data/entities/Book;Lio/legado/app/model/DebugLog;Lio/legado/app/data/entities/rule/TocRule;Lkotlin/jvm/internal/Ref$ObjectRef;Ljava/util/ArrayList;Lkotlin/coroutines/Continuation;)V
        //  1507: checkcast       Lkotlin/jvm/functions/Function2;
        //  1510: aload           $continuation
        //  1512: aload           $continuation
        //  1514: aload_1        
        //  1515: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //  1518: aload           $continuation
        //  1520: aload           6
        //  1522: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //  1525: aload           $continuation
        //  1527: aload           8
        //  1529: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //  1532: aload           $continuation
        //  1534: aconst_null    
        //  1535: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$3:Ljava/lang/Object;
        //  1538: aload           $continuation
        //  1540: aconst_null    
        //  1541: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$4:Ljava/lang/Object;
        //  1544: aload           $continuation
        //  1546: aconst_null    
        //  1547: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$5:Ljava/lang/Object;
        //  1550: aload           $continuation
        //  1552: aconst_null    
        //  1553: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$6:Ljava/lang/Object;
        //  1556: aload           $continuation
        //  1558: aconst_null    
        //  1559: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$7:Ljava/lang/Object;
        //  1562: aload           $continuation
        //  1564: aconst_null    
        //  1565: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$8:Ljava/lang/Object;
        //  1568: aload           $continuation
        //  1570: iload           11
        //  1572: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //  1575: aload           $continuation
        //  1577: iconst_4       
        //  1578: putfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.label:I
        //  1581: invokestatic    kotlinx/coroutines/BuildersKt.withContext:(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1584: dup            
        //  1585: aload           30
        //  1587: if_acmpne       1636
        //  1590: aload           30
        //  1592: areturn        
        //  1593: aload           $continuation
        //  1595: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.I$0:I
        //  1598: istore          11
        //  1600: aload           $continuation
        //  1602: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$2:Ljava/lang/Object;
        //  1605: checkcast       Ljava/util/ArrayList;
        //  1608: astore          8
        //  1610: aload           $continuation
        //  1612: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$1:Ljava/lang/Object;
        //  1615: checkcast       Lio/legado/app/model/DebugLog;
        //  1618: astore          6
        //  1620: aload           $continuation
        //  1622: getfield        io/legado/app/model/webBook/BookChapterList$analyzeChapterList$1.L$0:Ljava/lang/Object;
        //  1625: checkcast       Lio/legado/app/data/entities/Book;
        //  1628: astore_1       
        //  1629: aload           $result
        //  1631: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1634: aload           $result
        //  1636: pop            
        //  1637: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1640: pop            
        //  1641: aload           8
        //  1643: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //  1646: ifeq            1660
        //  1649: new             Lio/legado/app/exception/TocEmptyException;
        //  1652: dup            
        //  1653: ldc_w           "\u76ee\u5f55\u4e3a\u7a7a"
        //  1656: invokespecial   io/legado/app/exception/TocEmptyException.<init>:(Ljava/lang/String;)V
        //  1659: athrow         
        //  1660: iload           11
        //  1662: ifne            1673
        //  1665: aload           8
        //  1667: checkcast       Ljava/util/List;
        //  1670: invokestatic    kotlin/collections/CollectionsKt.reverse:(Ljava/util/List;)V
        //  1673: aload           $continuation
        //  1675: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //  1680: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //  1683: new             Ljava/util/LinkedHashSet;
        //  1686: dup            
        //  1687: aload           8
        //  1689: checkcast       Ljava/util/Collection;
        //  1692: invokespecial   java/util/LinkedHashSet.<init>:(Ljava/util/Collection;)V
        //  1695: astore          lh
        //  1697: new             Ljava/util/ArrayList;
        //  1700: dup            
        //  1701: aload           lh
        //  1703: checkcast       Ljava/util/Collection;
        //  1706: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
        //  1709: astore          list
        //  1711: aload           list
        //  1713: checkcast       Ljava/util/List;
        //  1716: invokestatic    kotlin/collections/CollectionsKt.reverse:(Ljava/util/List;)V
        //  1719: aload           6
        //  1721: astore          16
        //  1723: aload           16
        //  1725: ifnonnull       1732
        //  1728: aconst_null    
        //  1729: goto            1761
        //  1732: aload           16
        //  1734: aload_1        
        //  1735: invokevirtual   io/legado/app/data/entities/Book.getOrigin:()Ljava/lang/String;
        //  1738: ldc_w           "\u25c7\u76ee\u5f55\u603b\u6570:"
        //  1741: aload           list
        //  1743: invokevirtual   java/util/ArrayList.size:()I
        //  1746: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //  1749: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1752: iconst_0       
        //  1753: iconst_4       
        //  1754: aconst_null    
        //  1755: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1758: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1761: pop            
        //  1762: aload           $continuation
        //  1764: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //  1769: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //  1772: aload           list
        //  1774: checkcast       Ljava/lang/Iterable;
        //  1777: astore          $this$forEachIndexed$iv
        //  1779: iconst_0       
        //  1780: istore          $i$f$forEachIndexed
        //  1782: iconst_0       
        //  1783: istore          index$iv
        //  1785: aload           $this$forEachIndexed$iv
        //  1787: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //  1792: astore          19
        //  1794: aload           19
        //  1796: invokeinterface java/util/Iterator.hasNext:()Z
        //  1801: ifeq            1865
        //  1804: aload           19
        //  1806: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1811: astore          item$iv
        //  1813: iload           index$iv
        //  1815: iinc            index$iv, 1
        //  1818: istore          21
        //  1820: iconst_0       
        //  1821: istore          22
        //  1823: iload           21
        //  1825: ifge            1831
        //  1828: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //  1831: iload           21
        //  1833: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //  1836: aload           item$iv
        //  1838: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1841: astore          23
        //  1843: checkcast       Ljava/lang/Number;
        //  1846: invokevirtual   java/lang/Number.intValue:()I
        //  1849: istore          index
        //  1851: iconst_0       
        //  1852: istore          $i$a$-forEachIndexed-BookChapterList$analyzeChapterList$4
        //  1854: aload           bookChapter
        //  1856: iload           index
        //  1858: invokevirtual   io/legado/app/data/entities/BookChapter.setIndex:(I)V
        //  1861: nop            
        //  1862: goto            1794
        //  1865: nop            
        //  1866: aload           list
        //  1868: invokevirtual   java/util/ArrayList.size:()I
        //  1871: ifle            1892
        //  1874: aload_1        
        //  1875: aload           list
        //  1877: checkcast       Ljava/util/List;
        //  1880: invokestatic    kotlin/collections/CollectionsKt.last:(Ljava/util/List;)Ljava/lang/Object;
        //  1883: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1886: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //  1889: invokevirtual   io/legado/app/data/entities/Book.setLatestChapterTitle:(Ljava/lang/String;)V
        //  1892: aload_1        
        //  1893: invokevirtual   io/legado/app/data/entities/Book.getTotalChapterNum:()I
        //  1896: aload           list
        //  1898: invokevirtual   java/util/ArrayList.size:()I
        //  1901: if_icmpge       1918
        //  1904: aload_1        
        //  1905: aload           list
        //  1907: invokevirtual   java/util/ArrayList.size:()I
        //  1910: aload_1        
        //  1911: invokevirtual   io/legado/app/data/entities/Book.getTotalChapterNum:()I
        //  1914: isub           
        //  1915: invokevirtual   io/legado/app/data/entities/Book.setLastCheckCount:(I)V
        //  1918: aload_1        
        //  1919: aload           list
        //  1921: invokevirtual   java/util/ArrayList.size:()I
        //  1924: invokevirtual   io/legado/app/data/entities/Book.setTotalChapterNum:(I)V
        //  1927: aload           $continuation
        //  1929: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //  1934: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //  1937: aload           list
        //  1939: areturn        
        //  1940: new             Ljava/lang/IllegalStateException;
        //  1943: dup            
        //  1944: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //  1947: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  1950: athrow         
        //    Signature:
        //  (Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation<-Ljava/util/List<Lio/legado/app/data/entities/BookChapter;>;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  book         
        //  body         
        //  bookSource   
        //  baseUrl      
        //  redirectUrl  
        //  debugLog     
        //  $completion  
        //    StackMapTable: 00 2D 29 FF 00 0B 00 1E 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 18 00 00 FF 00 32 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 16 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 5A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 1B 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 A9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 55 07 00 50 FF 00 38 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 5A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 01 07 00 62 FF 00 01 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 5A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 02 07 00 62 07 00 5A FF 00 3A 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 5A 01 01 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 02 07 00 62 07 00 5A FF 00 0D 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 5A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 34 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 5A 01 01 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 02 07 00 62 07 00 5A FF 00 0D 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 5A 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 7A 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 65 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 00 00 00 00 00 00 00 00 00 00 00 07 00 62 00 07 00 04 07 00 18 07 00 04 00 01 07 00 04 FF 00 4A 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 01 00 00 00 00 00 00 00 00 00 00 00 07 00 62 07 00 04 07 00 04 07 00 18 07 00 04 00 00 05 FF 00 21 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 1A 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 07 00 C5 01 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 40 01 FF 00 AE 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 65 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 01 07 00 04 FF 00 10 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 07 00 5A 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 98 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 72 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 00 00 00 00 00 01 07 00 62 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 01 07 00 04 FF 00 2A 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 00 00 00 00 00 01 07 00 62 07 00 04 07 00 5A 00 00 00 07 00 04 07 00 18 07 00 04 00 01 07 00 62 FF 00 01 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 00 00 00 00 00 01 07 00 62 07 00 04 07 00 5A 00 00 00 07 00 04 07 00 18 07 00 04 00 02 07 00 62 07 00 5A FF 00 18 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 01 07 01 8E FF 00 03 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 07 00 C5 01 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 0C 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 00 07 00 62 07 00 A9 01 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 1E 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 01 00 00 00 00 00 00 00 00 00 00 00 07 00 62 07 00 04 07 00 04 07 00 18 07 00 04 00 00 FF 00 0C 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 07 00 65 07 00 39 01 07 00 62 07 00 62 01 07 00 A9 00 00 00 00 00 00 00 00 00 00 07 00 62 07 00 04 07 00 04 07 00 18 07 00 04 00 00 6A 07 00 50 FF 00 70 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 2A 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 01 07 00 04 43 07 00 50 13 0C FF 00 3A 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 00 00 01 00 00 07 01 31 07 00 39 07 00 A9 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 5C 07 00 50 FF 00 20 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 00 00 01 00 00 07 01 31 07 00 39 07 01 3C 01 01 07 01 42 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 24 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 00 00 01 00 00 07 01 31 07 00 39 07 01 3C 01 01 07 01 42 07 00 04 01 01 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 FF 00 21 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 07 00 39 00 00 01 00 00 07 01 31 07 00 39 07 01 3C 01 01 07 01 42 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00 1A 19 FF 00 15 00 1F 07 00 02 07 00 AB 07 00 5A 07 00 3C 07 00 5A 07 00 5A 07 00 A9 07 01 25 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 18 07 00 04 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final Object analyzeChapterList(final Book book, final String baseUrl, final String redirectUrl, final String body, final TocRule tocRule, final String listRule, final BookSource bookSource, final boolean getNextUrl, final boolean log, final DebugLog debugLog, final Continuation<? super Pair<? extends List<BookChapter>, ? extends List<String>>> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_1         /* book */
        //     5: checkcast       Lio/legado/app/model/analyzeRule/RuleDataInterface;
        //     8: aload           bookSource
        //    10: checkcast       Lio/legado/app/data/entities/BaseSource;
        //    13: aload           debugLog
        //    15: invokespecial   io/legado/app/model/analyzeRule/AnalyzeRule.<init>:(Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/DebugLog;)V
        //    18: astore          analyzeRule
        //    20: aload           analyzeRule
        //    22: aload           body
        //    24: aconst_null    
        //    25: iconst_2       
        //    26: aconst_null    
        //    27: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.setContent$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //    30: aload_2         /* baseUrl */
        //    31: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setBaseUrl:(Ljava/lang/String;)Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //    34: pop            
        //    35: aload           analyzeRule
        //    37: aload_3         /* redirectUrl */
        //    38: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setRedirectUrl:(Ljava/lang/String;)Ljava/net/URL;
        //    41: pop            
        //    42: iconst_0       
        //    43: istore          14
        //    45: new             Ljava/util/ArrayList;
        //    48: dup            
        //    49: invokespecial   java/util/ArrayList.<init>:()V
        //    52: astore          chapterList
        //    54: iload           log
        //    56: ifeq            87
        //    59: aload           debugLog
        //    61: astore          14
        //    63: aload           14
        //    65: ifnonnull       71
        //    68: goto            87
        //    71: aload           14
        //    73: aload           bookSource
        //    75: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //    78: ldc_w           "\u250c\u83b7\u53d6\u76ee\u5f55\u5217\u8868"
        //    81: iconst_0       
        //    82: iconst_4       
        //    83: aconst_null    
        //    84: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //    87: aload           analyzeRule
        //    89: aload           listRule
        //    91: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.getElements:(Ljava/lang/String;)Ljava/util/List;
        //    94: astore          elements
        //    96: iload           log
        //    98: ifeq            142
        //   101: aload           debugLog
        //   103: astore          15
        //   105: aload           15
        //   107: ifnonnull       113
        //   110: goto            142
        //   113: aload           15
        //   115: aload           bookSource
        //   117: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   120: ldc_w           "\u2514\u5217\u8868\u5927\u5c0f:"
        //   123: aload           elements
        //   125: invokeinterface java/util/List.size:()I
        //   130: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   133: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   136: iconst_0       
        //   137: iconst_4       
        //   138: aconst_null    
        //   139: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   142: iconst_0       
        //   143: istore          16
        //   145: new             Ljava/util/ArrayList;
        //   148: dup            
        //   149: invokespecial   java/util/ArrayList.<init>:()V
        //   152: astore          nextUrlList
        //   154: aload           tocRule
        //   156: invokevirtual   io/legado/app/data/entities/rule/TocRule.getNextTocUrl:()Ljava/lang/String;
        //   159: astore          nextTocRule
        //   161: iload           getNextUrl
        //   163: ifeq            376
        //   166: aload           nextTocRule
        //   168: checkcast       Ljava/lang/CharSequence;
        //   171: astore          17
        //   173: iconst_0       
        //   174: istore          18
        //   176: iconst_0       
        //   177: istore          19
        //   179: aload           17
        //   181: ifnull          194
        //   184: aload           17
        //   186: invokeinterface java/lang/CharSequence.length:()I
        //   191: ifne            198
        //   194: iconst_1       
        //   195: goto            199
        //   198: iconst_0       
        //   199: ifne            376
        //   202: iload           log
        //   204: ifeq            235
        //   207: aload           debugLog
        //   209: astore          17
        //   211: aload           17
        //   213: ifnonnull       219
        //   216: goto            235
        //   219: aload           17
        //   221: aload           bookSource
        //   223: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   226: ldc_w           "\u250c\u83b7\u53d6\u76ee\u5f55\u4e0b\u4e00\u9875\u5217\u8868"
        //   229: iconst_0       
        //   230: iconst_4       
        //   231: aconst_null    
        //   232: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   235: aload           analyzeRule
        //   237: aload           nextTocRule
        //   239: aconst_null    
        //   240: iconst_1       
        //   241: iconst_2       
        //   242: aconst_null    
        //   243: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.getStringList$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;Ljava/lang/Object;ZILjava/lang/Object;)Ljava/util/List;
        //   246: astore          17
        //   248: aload           17
        //   250: ifnonnull       256
        //   253: goto            326
        //   256: aload           17
        //   258: astore          18
        //   260: iconst_0       
        //   261: istore          19
        //   263: iconst_0       
        //   264: istore          20
        //   266: aload           18
        //   268: astore          it
        //   270: iconst_0       
        //   271: istore          $i$a$-let-BookChapterList$analyzeChapterList$6
        //   273: aload           it
        //   275: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   280: astore          23
        //   282: aload           23
        //   284: invokeinterface java/util/Iterator.hasNext:()Z
        //   289: ifeq            324
        //   292: aload           23
        //   294: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   299: checkcast       Ljava/lang/String;
        //   302: astore          item
        //   304: aload           item
        //   306: aload_3         /* redirectUrl */
        //   307: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   310: ifne            282
        //   313: aload           nextUrlList
        //   315: aload           item
        //   317: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   320: pop            
        //   321: goto            282
        //   324: nop            
        //   325: nop            
        //   326: iload           log
        //   328: ifeq            376
        //   331: aload           debugLog
        //   333: astore          17
        //   335: aload           17
        //   337: ifnonnull       343
        //   340: goto            376
        //   343: aload           17
        //   345: aload           bookSource
        //   347: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   350: ldc_w           "\u2514"
        //   353: ldc_w           "\uff0c\n"
        //   356: checkcast       Ljava/lang/CharSequence;
        //   359: aload           nextUrlList
        //   361: checkcast       Ljava/lang/Iterable;
        //   364: invokestatic    io/legado/app/utils/TextUtils.join:(Ljava/lang/CharSequence;Ljava/lang/Iterable;)Ljava/lang/String;
        //   367: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   370: iconst_0       
        //   371: iconst_4       
        //   372: aconst_null    
        //   373: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   376: aload           $completion
        //   378: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   383: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //   386: aload           elements
        //   388: checkcast       Ljava/util/Collection;
        //   391: astore          17
        //   393: iconst_0       
        //   394: istore          18
        //   396: aload           17
        //   398: invokeinterface java/util/Collection.isEmpty:()Z
        //   403: ifne            410
        //   406: iconst_1       
        //   407: goto            411
        //   410: iconst_0       
        //   411: ifeq            1365
        //   414: iload           log
        //   416: ifeq            447
        //   419: aload           debugLog
        //   421: astore          17
        //   423: aload           17
        //   425: ifnonnull       431
        //   428: goto            447
        //   431: aload           17
        //   433: aload           bookSource
        //   435: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   438: ldc_w           "\u250c\u89e3\u6790\u76ee\u5f55\u5217\u8868"
        //   441: iconst_0       
        //   442: iconst_4       
        //   443: aconst_null    
        //   444: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   447: aload           analyzeRule
        //   449: aload           tocRule
        //   451: invokevirtual   io/legado/app/data/entities/rule/TocRule.getChapterName:()Ljava/lang/String;
        //   454: iconst_0       
        //   455: iconst_2       
        //   456: aconst_null    
        //   457: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   460: astore          nameRule
        //   462: aload           analyzeRule
        //   464: aload           tocRule
        //   466: invokevirtual   io/legado/app/data/entities/rule/TocRule.getChapterUrl:()Ljava/lang/String;
        //   469: iconst_0       
        //   470: iconst_2       
        //   471: aconst_null    
        //   472: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   475: astore          urlRule
        //   477: aload           analyzeRule
        //   479: aload           tocRule
        //   481: invokevirtual   io/legado/app/data/entities/rule/TocRule.isVip:()Ljava/lang/String;
        //   484: iconst_0       
        //   485: iconst_2       
        //   486: aconst_null    
        //   487: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   490: astore          vipRule
        //   492: aload           analyzeRule
        //   494: aload           tocRule
        //   496: invokevirtual   io/legado/app/data/entities/rule/TocRule.getUpdateTime:()Ljava/lang/String;
        //   499: iconst_0       
        //   500: iconst_2       
        //   501: aconst_null    
        //   502: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   505: astore          upTimeRule
        //   507: aload           analyzeRule
        //   509: aload           tocRule
        //   511: invokevirtual   io/legado/app/data/entities/rule/TocRule.isVolume:()Ljava/lang/String;
        //   514: iconst_0       
        //   515: iconst_2       
        //   516: aconst_null    
        //   517: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   520: astore          isVolumeRule
        //   522: aload           elements
        //   524: checkcast       Ljava/lang/Iterable;
        //   527: astore          $this$forEachIndexed$iv
        //   529: iconst_0       
        //   530: istore          $i$f$forEachIndexed
        //   532: iconst_0       
        //   533: istore          index$iv
        //   535: aload           $this$forEachIndexed$iv
        //   537: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   542: astore          25
        //   544: aload           25
        //   546: invokeinterface java/util/Iterator.hasNext:()Z
        //   551: ifeq            1059
        //   554: aload           25
        //   556: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   561: astore          item$iv
        //   563: iload           index$iv
        //   565: iinc            index$iv, 1
        //   568: istore          27
        //   570: iconst_0       
        //   571: istore          28
        //   573: iload           27
        //   575: ifge            581
        //   578: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   581: iload           27
        //   583: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   586: aload           item$iv
        //   588: astore          29
        //   590: checkcast       Ljava/lang/Number;
        //   593: invokevirtual   java/lang/Number.intValue:()I
        //   596: istore          index
        //   598: iconst_0       
        //   599: istore          $i$a$-forEachIndexed-BookChapterList$analyzeChapterList$7
        //   601: aload           $completion
        //   603: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   608: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //   611: aload           analyzeRule
        //   613: aload           item
        //   615: aconst_null    
        //   616: iconst_2       
        //   617: aconst_null    
        //   618: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.setContent$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //   621: pop            
        //   622: aload_1         /* book */
        //   623: invokevirtual   io/legado/app/data/entities/Book.getBookUrl:()Ljava/lang/String;
        //   626: astore          32
        //   628: new             Lio/legado/app/data/entities/BookChapter;
        //   631: dup            
        //   632: aconst_null    
        //   633: aconst_null    
        //   634: iconst_0       
        //   635: aload_3         /* redirectUrl */
        //   636: aload           32
        //   638: iconst_0       
        //   639: aconst_null    
        //   640: aconst_null    
        //   641: aconst_null    
        //   642: aconst_null    
        //   643: aconst_null    
        //   644: aconst_null    
        //   645: aconst_null    
        //   646: sipush          8167
        //   649: aconst_null    
        //   650: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   653: astore          bookChapter
        //   655: aload           analyzeRule
        //   657: aload           bookChapter
        //   659: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setChapter:(Lio/legado/app/data/entities/BookChapter;)V
        //   662: aload           bookChapter
        //   664: aload           analyzeRule
        //   666: aload           nameRule
        //   668: aconst_null    
        //   669: iconst_0       
        //   670: bipush          6
        //   672: aconst_null    
        //   673: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.getString$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/util/List;Ljava/lang/Object;ZILjava/lang/Object;)Ljava/lang/String;
        //   676: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //   679: aload           bookChapter
        //   681: aload           analyzeRule
        //   683: aload           urlRule
        //   685: aconst_null    
        //   686: iconst_0       
        //   687: bipush          6
        //   689: aconst_null    
        //   690: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.getString$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/util/List;Ljava/lang/Object;ZILjava/lang/Object;)Ljava/lang/String;
        //   693: invokevirtual   io/legado/app/data/entities/BookChapter.setUrl:(Ljava/lang/String;)V
        //   696: aload           bookChapter
        //   698: aload           analyzeRule
        //   700: aload           upTimeRule
        //   702: aconst_null    
        //   703: iconst_0       
        //   704: bipush          6
        //   706: aconst_null    
        //   707: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.getString$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/util/List;Ljava/lang/Object;ZILjava/lang/Object;)Ljava/lang/String;
        //   710: invokevirtual   io/legado/app/data/entities/BookChapter.setTag:(Ljava/lang/String;)V
        //   713: aload           bookChapter
        //   715: aload_1         /* book */
        //   716: invokevirtual   io/legado/app/data/entities/Book.getUserNameSpace:()Ljava/lang/String;
        //   719: invokevirtual   io/legado/app/data/entities/BookChapter.setUserNameSpace:(Ljava/lang/String;)V
        //   722: aload           analyzeRule
        //   724: aload           isVolumeRule
        //   726: aconst_null    
        //   727: iconst_0       
        //   728: bipush          6
        //   730: aconst_null    
        //   731: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.getString$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/util/List;Ljava/lang/Object;ZILjava/lang/Object;)Ljava/lang/String;
        //   734: astore          isVolume
        //   736: aload           bookChapter
        //   738: iconst_0       
        //   739: invokevirtual   io/legado/app/data/entities/BookChapter.setVolume:(Z)V
        //   742: aload           isVolume
        //   744: iconst_0       
        //   745: iconst_1       
        //   746: aconst_null    
        //   747: invokestatic    io/legado/app/utils/StringExtensionsKt.isTrue$default:(Ljava/lang/String;ZILjava/lang/Object;)Z
        //   750: ifeq            759
        //   753: aload           bookChapter
        //   755: iconst_1       
        //   756: invokevirtual   io/legado/app/data/entities/BookChapter.setVolume:(Z)V
        //   759: aload           bookChapter
        //   761: invokevirtual   io/legado/app/data/entities/BookChapter.getUrl:()Ljava/lang/String;
        //   764: checkcast       Ljava/lang/CharSequence;
        //   767: astore          34
        //   769: iconst_0       
        //   770: istore          35
        //   772: aload           34
        //   774: invokeinterface java/lang/CharSequence.length:()I
        //   779: ifne            786
        //   782: iconst_1       
        //   783: goto            787
        //   786: iconst_0       
        //   787: ifeq            939
        //   790: aload           bookChapter
        //   792: invokevirtual   io/legado/app/data/entities/BookChapter.isVolume:()Z
        //   795: ifeq            876
        //   798: aload           bookChapter
        //   800: aload           bookChapter
        //   802: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   805: iload           index
        //   807: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   810: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   813: invokevirtual   io/legado/app/data/entities/BookChapter.setUrl:(Ljava/lang/String;)V
        //   816: iload           log
        //   818: ifeq            939
        //   821: aload           debugLog
        //   823: astore          34
        //   825: aload           34
        //   827: ifnonnull       833
        //   830: goto            939
        //   833: aload           34
        //   835: aload           bookSource
        //   837: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   840: new             Ljava/lang/StringBuilder;
        //   843: dup            
        //   844: invokespecial   java/lang/StringBuilder.<init>:()V
        //   847: ldc_w           "\u21d2\u4e00\u7ea7\u76ee\u5f55"
        //   850: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   853: iload           index
        //   855: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   858: ldc_w           "\u672a\u83b7\u53d6\u5230url,\u4f7f\u7528\u6807\u9898\u66ff\u4ee3"
        //   861: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   864: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   867: iconst_0       
        //   868: iconst_4       
        //   869: aconst_null    
        //   870: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   873: goto            939
        //   876: aload           bookChapter
        //   878: aload_2         /* baseUrl */
        //   879: invokevirtual   io/legado/app/data/entities/BookChapter.setUrl:(Ljava/lang/String;)V
        //   882: iload           log
        //   884: ifeq            939
        //   887: aload           debugLog
        //   889: astore          34
        //   891: aload           34
        //   893: ifnonnull       899
        //   896: goto            939
        //   899: aload           34
        //   901: aload           bookSource
        //   903: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   906: new             Ljava/lang/StringBuilder;
        //   909: dup            
        //   910: invokespecial   java/lang/StringBuilder.<init>:()V
        //   913: ldc_w           "\u21d2\u76ee\u5f55"
        //   916: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   919: iload           index
        //   921: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   924: ldc_w           "\u672a\u83b7\u53d6\u5230url,\u4f7f\u7528baseUrl\u66ff\u4ee3"
        //   927: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   930: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   933: iconst_0       
        //   934: iconst_4       
        //   935: aconst_null    
        //   936: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   939: aload           bookChapter
        //   941: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   944: checkcast       Ljava/lang/CharSequence;
        //   947: astore          34
        //   949: iconst_0       
        //   950: istore          35
        //   952: aload           34
        //   954: invokeinterface java/lang/CharSequence.length:()I
        //   959: ifle            966
        //   962: iconst_1       
        //   963: goto            967
        //   966: iconst_0       
        //   967: ifeq            1022
        //   970: aload           analyzeRule
        //   972: aload           vipRule
        //   974: aconst_null    
        //   975: iconst_0       
        //   976: bipush          6
        //   978: aconst_null    
        //   979: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.getString$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/util/List;Ljava/lang/Object;ZILjava/lang/Object;)Ljava/lang/String;
        //   982: astore          isVip
        //   984: aload           isVip
        //   986: iconst_0       
        //   987: iconst_1       
        //   988: aconst_null    
        //   989: invokestatic    io/legado/app/utils/StringExtensionsKt.isTrue$default:(Ljava/lang/String;ZILjava/lang/Object;)Z
        //   992: ifeq            1011
        //   995: aload           bookChapter
        //   997: ldc_w           "\ud83d\udd12"
        //  1000: aload           bookChapter
        //  1002: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //  1005: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1008: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //  1011: aload           chapterList
        //  1013: aload           bookChapter
        //  1015: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1018: pop            
        //  1019: goto            1055
        //  1022: iload           log
        //  1024: ifeq            1055
        //  1027: aload           debugLog
        //  1029: astore          34
        //  1031: aload           34
        //  1033: ifnonnull       1039
        //  1036: goto            1055
        //  1039: aload           34
        //  1041: aload           bookSource
        //  1043: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1046: ldc_w           "\u7ae0\u8282\u540d\u4e3a\u7a7a"
        //  1049: iconst_0       
        //  1050: iconst_4       
        //  1051: aconst_null    
        //  1052: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1055: nop            
        //  1056: goto            544
        //  1059: nop            
        //  1060: iload           log
        //  1062: ifeq            1093
        //  1065: aload           debugLog
        //  1067: astore          22
        //  1069: aload           22
        //  1071: ifnonnull       1077
        //  1074: goto            1093
        //  1077: aload           22
        //  1079: aload           bookSource
        //  1081: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1084: ldc_w           "\u2514\u76ee\u5f55\u5217\u8868\u89e3\u6790\u5b8c\u6210"
        //  1087: iconst_0       
        //  1088: iconst_4       
        //  1089: aconst_null    
        //  1090: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1093: aload           chapterList
        //  1095: invokevirtual   java/util/ArrayList.size:()I
        //  1098: ifle            1332
        //  1101: iload           log
        //  1103: ifeq            1134
        //  1106: aload           debugLog
        //  1108: astore          22
        //  1110: aload           22
        //  1112: ifnonnull       1118
        //  1115: goto            1134
        //  1118: aload           22
        //  1120: aload           bookSource
        //  1122: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1125: ldc_w           "\u2261\u9996\u7ae0\u4fe1\u606f"
        //  1128: iconst_0       
        //  1129: iconst_4       
        //  1130: aconst_null    
        //  1131: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1134: iload           log
        //  1136: ifeq            1182
        //  1139: aload           debugLog
        //  1141: astore          22
        //  1143: aload           22
        //  1145: ifnonnull       1151
        //  1148: goto            1182
        //  1151: aload           22
        //  1153: aload           bookSource
        //  1155: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1158: ldc_w           "\u25c7\u7ae0\u8282\u540d\u79f0:"
        //  1161: aload           chapterList
        //  1163: iconst_0       
        //  1164: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1167: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1170: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //  1173: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1176: iconst_0       
        //  1177: iconst_4       
        //  1178: aconst_null    
        //  1179: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1182: iload           log
        //  1184: ifeq            1230
        //  1187: aload           debugLog
        //  1189: astore          22
        //  1191: aload           22
        //  1193: ifnonnull       1199
        //  1196: goto            1230
        //  1199: aload           22
        //  1201: aload           bookSource
        //  1203: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1206: ldc_w           "\u25c7\u7ae0\u8282\u94fe\u63a5:"
        //  1209: aload           chapterList
        //  1211: iconst_0       
        //  1212: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1215: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1218: invokevirtual   io/legado/app/data/entities/BookChapter.getUrl:()Ljava/lang/String;
        //  1221: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1224: iconst_0       
        //  1225: iconst_4       
        //  1226: aconst_null    
        //  1227: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1230: iload           log
        //  1232: ifeq            1278
        //  1235: aload           debugLog
        //  1237: astore          22
        //  1239: aload           22
        //  1241: ifnonnull       1247
        //  1244: goto            1278
        //  1247: aload           22
        //  1249: aload           bookSource
        //  1251: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1254: ldc_w           "\u25c7\u7ae0\u8282\u4fe1\u606f:"
        //  1257: aload           chapterList
        //  1259: iconst_0       
        //  1260: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1263: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1266: invokevirtual   io/legado/app/data/entities/BookChapter.getTag:()Ljava/lang/String;
        //  1269: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1272: iconst_0       
        //  1273: iconst_4       
        //  1274: aconst_null    
        //  1275: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1278: iload           log
        //  1280: ifeq            1365
        //  1283: aload           debugLog
        //  1285: astore          22
        //  1287: aload           22
        //  1289: ifnonnull       1295
        //  1292: goto            1365
        //  1295: aload           22
        //  1297: aload           bookSource
        //  1299: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1302: ldc_w           "\u25c7\u662f\u5426\u5377\u540d:"
        //  1305: aload           chapterList
        //  1307: iconst_0       
        //  1308: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1311: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1314: invokevirtual   io/legado/app/data/entities/BookChapter.isVolume:()Z
        //  1317: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxBoolean:(Z)Ljava/lang/Boolean;
        //  1320: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1323: iconst_0       
        //  1324: iconst_4       
        //  1325: aconst_null    
        //  1326: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1329: goto            1365
        //  1332: iload           log
        //  1334: ifeq            1365
        //  1337: aload           debugLog
        //  1339: astore          22
        //  1341: aload           22
        //  1343: ifnonnull       1349
        //  1346: goto            1365
        //  1349: aload           22
        //  1351: aload           bookSource
        //  1353: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1356: ldc_w           "\u7ae0\u8282\u5217\u8868\u4e3a\u7a7a"
        //  1359: iconst_0       
        //  1360: iconst_4       
        //  1361: aconst_null    
        //  1362: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1365: new             Lkotlin/Pair;
        //  1368: dup            
        //  1369: aload           chapterList
        //  1371: aload           nextUrlList
        //  1373: invokespecial   kotlin/Pair.<init>:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  1376: areturn        
        //    Signature:
        //  (Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/rule/TocRule;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;ZZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation<-Lkotlin/Pair<+Ljava/util/List<Lio/legado/app/data/entities/BookChapter;>;+Ljava/util/List<Ljava/lang/String;>;>;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  book         
        //  baseUrl      
        //  redirectUrl  
        //  body         
        //  tocRule      
        //  listRule     
        //  bookSource   
        //  getNextUrl   
        //  log          
        //  debugLog     
        //  $completion  
        //    StackMapTable: 00 31 FE 00 47 07 01 97 07 00 39 07 00 A9 FA 00 0F FD 00 19 07 00 BB 07 00 A9 FA 00 1C FF 00 33 00 14 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 C5 01 01 00 00 03 40 01 FF 00 13 00 14 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 A9 01 01 00 00 FF 00 0F 00 14 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 04 01 01 00 00 FF 00 14 00 14 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 01 01 00 00 FF 00 19 00 18 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 01 01 07 00 BB 01 07 01 42 00 00 29 FF 00 01 00 14 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 00 01 00 00 FF 00 10 00 14 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 A9 00 01 00 00 F8 00 20 FD 00 21 07 00 B2 01 40 01 FF 00 13 00 13 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 A9 01 00 00 FF 00 0F 00 13 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 04 01 00 00 FF 00 60 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 00 00 FE 00 24 07 00 04 01 01 FF 00 B1 00 22 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 00 00 FD 00 1A 07 00 C5 01 40 01 FF 00 2D 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 A9 01 00 00 FF 00 2A 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 C5 01 00 00 FF 00 16 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 A9 01 00 00 FF 00 27 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 04 01 00 00 FF 00 1A 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 C5 01 00 00 40 01 FF 00 2B 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 5A 01 00 00 FF 00 0A 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 C5 01 00 00 FF 00 10 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 A9 01 00 00 FF 00 0F 00 24 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 07 00 04 01 01 07 00 04 01 01 07 00 5A 07 01 4D 07 00 04 01 00 00 FF 00 03 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 01 3C 01 01 07 01 42 00 00 FF 00 11 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 A9 01 01 07 01 42 00 00 FF 00 0F 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 04 01 01 07 01 42 00 00 FF 00 18 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 A9 01 01 07 01 42 00 00 FF 00 0F 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 04 01 01 07 01 42 00 00 FF 00 10 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 A9 01 01 07 01 42 00 00 FF 00 1E 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 04 01 01 07 01 42 00 00 FF 00 10 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 A9 01 01 07 01 42 00 00 FF 00 1E 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 04 01 01 07 01 42 00 00 FF 00 10 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 A9 01 01 07 01 42 00 00 FF 00 1E 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 04 01 01 07 01 42 00 00 FF 00 10 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 A9 01 01 07 01 42 00 00 FF 00 24 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 04 01 01 07 01 42 00 00 FF 00 10 00 1A 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 BB 07 00 A9 01 01 07 01 42 00 00 FF 00 0F 00 12 07 00 02 07 00 AB 07 00 5A 07 00 5A 07 00 5A 07 00 65 07 00 5A 07 00 3C 01 01 07 00 A9 07 01 25 07 01 97 07 00 39 07 00 BB 07 00 39 07 00 5A 07 00 04 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static {
        INSTANCE = new BookChapterList();
    }
}
