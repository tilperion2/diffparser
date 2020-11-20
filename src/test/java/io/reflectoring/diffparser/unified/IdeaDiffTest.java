package io.reflectoring.diffparser.unified;

import io.reflectoring.diffparser.api.DiffParser;
import io.reflectoring.diffparser.api.UnifiedDiffParser;
import io.reflectoring.diffparser.api.model.Diff;
import io.reflectoring.diffparser.api.model.Hunk;
import io.reflectoring.diffparser.api.model.Line;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Tests the DiffParser with a diff created by the Intellij Idea IDE.
 * Tests target to cover additional cases with fully removed file in diff, fully added file and just modified.
 */
public class IdeaDiffTest {

    @Test
    public void testIdeaDiffParseWithAddedRemovedModifiedFiles() throws Exception {
        // given
        DiffParser parser = new UnifiedDiffParser();
        InputStream in = getClass().getResourceAsStream("idea_addRemoveModifyFiles.diff");

        // when
        List<Diff> diffs = parser.parse(in);

        // then
        Assert.assertNotNull(diffs);
        Assert.assertEquals(3, diffs.size());

        //==============================================================================================================
        Diff diff1 = diffs.get(0);
        Assert.assertEquals(1, diff1.getHunks().size());
        Assert.assertEquals(2, diff1.getHeaderLines().size());

        assertEquals("src/main/java/io/reflectoring/diffparser/api/model/Diff.java", diff1.getFromFileName());
        assertEquals("src/main/java/io/reflectoring/diffparser/api/model/Diff.java", diff1.getToFileName());

        Hunk hunk1_1 = diff1.getHunks().get(0);
        assertEquals(1, hunk1_1.getFromFileRange().getLineStart());
        assertEquals(99, hunk1_1.getFromFileRange().getLineCount());
        assertEquals(0, hunk1_1.getToFileRange().getLineStart());
        assertEquals(0, hunk1_1.getToFileRange().getLineCount());

        List<Line> lines = hunk1_1.getLines();
        assertEquals(99, lines.size());
        assertTrue(lines.stream().allMatch(l -> l.getLineType().equals(Line.LineType.FROM)));
        //==============================================================================================================

        Diff diff2 = diffs.get(1);
        Assert.assertEquals(1, diff2.getHunks().size());
        Assert.assertEquals(5, diff2.getHeaderLines().size());

        assertEquals("src/main/java/io/reflectoring/diffparser/api/model/SomeClass.java", diff2.getFromFileName());
        assertEquals("src/main/java/io/reflectoring/diffparser/api/model/SomeClass.java", diff2.getToFileName());

        Hunk hunk2_1 = diff2.getHunks().get(0);
        assertEquals(0, hunk2_1.getFromFileRange().getLineStart());
        assertEquals(0, hunk2_1.getFromFileRange().getLineCount());
        assertEquals(1, hunk2_1.getToFileRange().getLineStart());
        assertEquals(8, hunk2_1.getToFileRange().getLineCount());

        lines = hunk2_1.getLines();
        assertEquals(8, lines.size());
        assertTrue(lines.stream().allMatch(l -> l.getLineType().equals(Line.LineType.TO)));
        //==============================================================================================================

        Diff diff3 = diffs.get(2);
        Assert.assertEquals(3, diff3.getHunks().size());
        Assert.assertEquals(5, diff3.getHeaderLines().size());

        assertEquals("build.gradle", diff3.getFromFileName());
        assertEquals("build.gradle", diff3.getToFileName());

        Hunk hunk3_1 = diff3.getHunks().get(0);
        assertEquals(1, hunk3_1.getFromFileRange().getLineStart());
        assertEquals(6, hunk3_1.getFromFileRange().getLineCount());
        assertEquals(1, hunk3_1.getToFileRange().getLineStart());
        assertEquals(6, hunk3_1.getToFileRange().getLineCount());

        lines = hunk3_1.getLines();
        assertEquals(7, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(2).getLineType());
        assertEquals(Line.LineType.TO, lines.get(3).getLineType());

        Hunk hunk3_2 = diff3.getHunks().get(1);
        assertEquals(13, hunk3_2.getFromFileRange().getLineStart());
        assertEquals(7, hunk3_2.getFromFileRange().getLineCount());
        assertEquals(13, hunk3_2.getToFileRange().getLineStart());
        assertEquals(7, hunk3_2.getToFileRange().getLineCount());

        lines = hunk3_2.getLines();
        assertEquals(8, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals(Line.LineType.TO, lines.get(4).getLineType());

        Hunk hunk3_3 = diff3.getHunks().get(2);
        assertEquals(24, hunk3_3.getFromFileRange().getLineStart());
        assertEquals(7, hunk3_3.getFromFileRange().getLineCount());
        assertEquals(24, hunk3_3.getToFileRange().getLineStart());
        assertEquals(12, hunk3_3.getToFileRange().getLineCount());

        lines = hunk3_3.getLines();
        assertEquals(13, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals(Line.LineType.TO, lines.get(4).getLineType());
        assertEquals(Line.LineType.TO, lines.get(5).getLineType());
        assertEquals(Line.LineType.TO, lines.get(6).getLineType());
        assertEquals(Line.LineType.TO, lines.get(7).getLineType());
        assertEquals(Line.LineType.TO, lines.get(8).getLineType());
        assertEquals(Line.LineType.TO, lines.get(9).getLineType());
        //==============================================================================================================
    }

}
