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

/**
 * Tests the DiffParser with a diff created by the some generic tool that do not add even any header to diff parts.
 */
public class GenericDiffSQLTest {

    @Test
    public void testGenericDiffParse() throws Exception {
        // given
        DiffParser parser = new UnifiedDiffParser();
        InputStream in = getClass().getResourceAsStream("generic_sql.diff");

        // when
        List<Diff> diffs = parser.parse(in);

        // then
        Assert.assertNotNull(diffs);
        Assert.assertEquals(3, diffs.size());

        //==============================================================================================================
        Diff diff1 = diffs.get(0);
        assertEquals("/database/schema.sql", diff1.getFromFileName());
        assertEquals("/database/schema.sql", diff1.getToFileName());
        assertEquals(1, diff1.getHunks().size());

        List<String> headerLines = diff1.getHeaderLines();
        assertEquals(0, headerLines.size());

        Hunk hunk1_1 = diff1.getHunks().get(0);
        assertEquals(11, hunk1_1.getFromFileRange().getLineStart());
        assertEquals(36, hunk1_1.getFromFileRange().getLineCount());
        assertEquals(11, hunk1_1.getToFileRange().getLineStart());
        assertEquals(42, hunk1_1.getToFileRange().getLineCount());

        List<Line> lines = hunk1_1.getLines();
        assertEquals(36, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(17).getLineType());
        assertEquals(Line.LineType.FROM, lines.get(18).getLineType());

        //==============================================================================================================
        Diff diff2 = diffs.get(1);
        assertEquals("/database/schema2.sql", diff2.getFromFileName());
        assertEquals("/database/schema2.sql", diff2.getToFileName());
        assertEquals(2, diff2.getHunks().size());

        headerLines = diff2.getHeaderLines();
        assertEquals(0, headerLines.size());

        Hunk hunk2_1 = diff2.getHunks().get(0);
        assertEquals(55, hunk2_1.getFromFileRange().getLineStart());
        assertEquals(19, hunk2_1.getFromFileRange().getLineCount());
        assertEquals(55, hunk2_1.getToFileRange().getLineStart());
        assertEquals(19, hunk2_1.getToFileRange().getLineCount());

        lines = hunk2_1.getLines();
        assertEquals(16, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals(Line.LineType.TO, lines.get(4).getLineType());
        assertEquals(Line.LineType.FROM, lines.get(11).getLineType());
        assertEquals(Line.LineType.TO, lines.get(12).getLineType());

        Hunk hunk2_2 = diff2.getHunks().get(1);
        assertEquals(140, hunk2_2.getFromFileRange().getLineStart());
        assertEquals(34, hunk2_2.getFromFileRange().getLineCount());
        assertEquals(140, hunk2_2.getToFileRange().getLineStart());
        assertEquals(44, hunk2_2.getToFileRange().getLineCount());

        lines = hunk2_2.getLines();
        assertEquals(40, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals(Line.LineType.FROM, lines.get(4).getLineType());
        assertEquals(Line.LineType.FROM, lines.get(5).getLineType());
        assertEquals(Line.LineType.FROM, lines.get(6).getLineType());
        assertEquals(Line.LineType.TO, lines.get(7).getLineType());

        //==============================================================================================================
        Diff diff3 = diffs.get(2);
        assertEquals("/database/migrate.xml", diff3.getFromFileName());
        assertEquals("/database/migrate.xml", diff3.getToFileName());
        assertEquals(1, diff3.getHunks().size());

        headerLines = diff3.getHeaderLines();
        assertEquals(0, headerLines.size());

        Hunk hunk3_1 = diff3.getHunks().get(0);
        assertEquals(30, hunk3_1.getFromFileRange().getLineStart());
        assertEquals(6, hunk3_1.getFromFileRange().getLineCount());
        assertEquals(30, hunk3_1.getToFileRange().getLineStart());
        assertEquals(7, hunk3_1.getToFileRange().getLineCount());

        lines = hunk3_1.getLines();
        assertEquals(7, lines.size());
        assertEquals(Line.LineType.TO, lines.get(3).getLineType());

    }

}
