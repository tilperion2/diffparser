package io.reflectoring.diffparser.unified;

import io.reflectoring.diffparser.api.DiffParser;
import io.reflectoring.diffparser.api.UnifiedDiffParser;
import io.reflectoring.diffparser.api.model.Diff;
import io.reflectoring.diffparser.api.model.Hunk;
import io.reflectoring.diffparser.api.model.Line;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Tests the DiffParser with a diff created by the some generic tool that do not add even any header to diff parts.
 */
public class GenericDiffTest {

    @Test
    public void testGenericDiffParse() throws Exception {
        // given
        DiffParser parser = new UnifiedDiffParser();
        InputStream in = getClass().getResourceAsStream("generic.diff");

        // when
        List<Diff> diffs = parser.parse(in);

        // then
        Assert.assertNotNull(diffs);
        Assert.assertEquals(2, diffs.size());

        //==============================================================================================================
        Diff diff1 = diffs.get(0);
        assertEquals("/dev/null", diff1.getFromFileName());
        assertEquals("//depot/SomeClass.java", diff1.getToFileName());
        assertEquals(1, diff1.getHunks().size());

        List<String> headerLines = diff1.getHeaderLines();
        assertEquals(0, headerLines.size());

        Hunk hunk1_1 = diff1.getHunks().get(0);
        assertEquals(0, hunk1_1.getFromFileRange().getLineStart());
        assertEquals(0, hunk1_1.getFromFileRange().getLineCount());
        assertEquals(1, hunk1_1.getToFileRange().getLineStart());
        assertEquals(12, hunk1_1.getToFileRange().getLineCount());

        List<Line> lines = hunk1_1.getLines();
        assertEquals(12, lines.size());
        assertEquals(Line.LineType.TO, lines.get(4).getLineType());

        //==============================================================================================================
        Diff diff2 = diffs.get(1);
        assertEquals("//depot/SomeOtherClass.java", diff2.getFromFileName());
        assertEquals("/depot/SomeOtherClass.java", diff2.getToFileName());
        assertEquals(1, diff2.getHunks().size());

        headerLines = diff2.getHeaderLines();
        assertEquals(0, headerLines.size());

        Hunk hunk2_1 = diff2.getHunks().get(0);
        assertEquals(21, hunk2_1.getFromFileRange().getLineStart());
        assertEquals(7, hunk2_1.getFromFileRange().getLineCount());
        assertEquals(21, hunk2_1.getToFileRange().getLineStart());
        assertEquals(6, hunk2_1.getToFileRange().getLineCount());

        lines = hunk2_1.getLines();
        assertEquals(7, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
    }

}
