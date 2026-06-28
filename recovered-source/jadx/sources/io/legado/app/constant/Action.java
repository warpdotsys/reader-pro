package io.legado.app.constant;

import kotlin.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: Action.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/constant/Action.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lio/legado/app/constant/Action;", PackageDocumentBase.PREFIX_OPF, "()V", Action.addTimer, PackageDocumentBase.PREFIX_OPF, Action.adjustProgress, Action.init, Action.moveTo, Action.next, Action.nextParagraph, Action.pause, Action.play, Action.prev, Action.prevParagraph, Action.resume, Action.setTimer, Action.stop, Action.upTtsSpeechRate, "reader-pro"})
public final class Action {

    @NotNull
    public static final Action INSTANCE = new Action();

    @NotNull
    public static final String play = "play";

    @NotNull
    public static final String stop = "stop";

    @NotNull
    public static final String resume = "resume";

    @NotNull
    public static final String pause = "pause";

    @NotNull
    public static final String addTimer = "addTimer";

    @NotNull
    public static final String setTimer = "setTimer";

    @NotNull
    public static final String prevParagraph = "prevParagraph";

    @NotNull
    public static final String nextParagraph = "nextParagraph";

    @NotNull
    public static final String upTtsSpeechRate = "upTtsSpeechRate";

    @NotNull
    public static final String adjustProgress = "adjustProgress";

    @NotNull
    public static final String prev = "prev";

    @NotNull
    public static final String next = "next";

    @NotNull
    public static final String moveTo = "moveTo";

    @NotNull
    public static final String init = "init";

    private Action() {
    }
}
