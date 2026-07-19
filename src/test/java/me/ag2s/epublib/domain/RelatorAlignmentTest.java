package me.ag2s.epublib.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class RelatorAlignmentTest {

    @Test
    void restoresJarConstantsBetweenEditorAndLibeleeAppellee() {
        Relator[] values = Relator.values();

        assertEquals(220, values.length);
        assertEquals(
                List.of(
                        "ELECTRICIAN", "ELECTROTYPER", "ENGINEER", "ENGRAVER", "ETCHER",
                        "EVENT_PLACE", "EXPERT", "FACSIMILIST", "FIELD_DIRECTOR", "FILM_EDITOR",
                        "FIRST_PARTY", "FORGER", "FORMER_OWNER", "FUNDER",
                        "GEOGRAPHIC_INFORMATION_SPECIALIST", "HONOREE", "HOST", "ILLUMINATOR",
                        "ILLUSTRATOR", "INSCRIBER", "INSTRUMENTALIST", "INTERVIEWEE", "INTERVIEWER",
                        "INVENTOR", "LABORATORY", "LABORATORY_DIRECTOR", "LANDSCAPE_ARCHITECT",
                        "LEAD", "LENDER", "LIBELANT", "LIBELANT_APPELLANT", "LIBELANT_APPELLEE",
                        "LIBELEE", "LIBELEE_APPELLANT"
                ),
                Arrays.stream(values)
                        .skip(Relator.EDITOR.ordinal() + 1L)
                        .limit(Relator.LIBELEE_APPELLEE.ordinal() - Relator.EDITOR.ordinal() - 1L)
                        .map(Enum::name)
                        .toList()
        );
        assertEquals("elg", Relator.ELECTRICIAN.getCode());
        assertEquals("Electrician", Relator.ELECTRICIAN.getName());
        assertEquals("gis", Relator.GEOGRAPHIC_INFORMATION_SPECIALIST.getCode());
        assertEquals("Geographic information specialist", Relator.GEOGRAPHIC_INFORMATION_SPECIALIST.getName());
        assertEquals("let", Relator.LIBELEE_APPELLANT.getCode());
        assertEquals("Libelee-appellant", Relator.LIBELEE_APPELLANT.getName());
    }
}
