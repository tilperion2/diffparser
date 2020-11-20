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
 * Tests the DiffParser with a diff created by the Intellij Idea IDE and that contains changes in SQLscripts.
 * Tests target to cover situation when diff has --- or +++ content, that does not represent diff from-to file lines.
 */
public class SqlDiffTest {

    @Test
    public void testSimpleSqlDiffParse() throws Exception {
        // given
        DiffParser parser = new UnifiedDiffParser();
        InputStream in = getClass().getResourceAsStream("idea_sql.diff");

        // when
        List<Diff> diffs = parser.parse(in);

        // then
        Assert.assertNotNull(diffs);
        Assert.assertEquals(1, diffs.size());

        //==============================================================================================================
        Diff diff1 = diffs.get(0);
        Assert.assertEquals(1, diff1.getHunks().size());
        Assert.assertEquals(5, diff1.getHeaderLines().size());

        assertEquals("database/sql.sql", diff1.getFromFileName());
        assertEquals("database/sql.sql", diff1.getToFileName());

        //==============================================================================================================
        Hunk hunk1_1 = diff1.getHunks().get(0);
        assertEquals(1, hunk1_1.getFromFileRange().getLineStart());
        assertEquals(11, hunk1_1.getFromFileRange().getLineCount());
        assertEquals(1, hunk1_1.getToFileRange().getLineStart());
        assertEquals(11, hunk1_1.getToFileRange().getLineCount());

        List<Line> lines = hunk1_1.getLines();
        assertEquals(14, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals("  SELECT * FROM SOME_TABLE -- removed line with valid select steatement", lines.get(3).getContent());
        assertEquals(Line.LineType.FROM, lines.get(4).getLineType());
        assertEquals("-- @author someAuthor -- valid removed comment line from sql file", lines.get(4).getContent());
        assertEquals(Line.LineType.FROM, lines.get(5).getLineType());
        assertEquals("-- @issue 1234 -- valid removed comment line from sql file", lines.get(5).getContent());

        assertEquals(Line.LineType.TO, lines.get(8).getLineType());
        assertEquals("  SELECT * FROM NEW_TABLE", lines.get(8).getContent());
        assertEquals(Line.LineType.TO, lines.get(9).getLineType());
        assertEquals("-- some new comments -- valid added comment line from sql file", lines.get(9).getContent());
        assertEquals(Line.LineType.TO, lines.get(10).getLineType());
        assertEquals("-- in sql file -- valid added comment line from sql file", lines.get(10).getContent());
        assertEquals(Line.LineType.TO, lines.get(11).getLineType());
        assertEquals("", lines.get(11).getContent());
        //==============================================================================================================
    }

    @Test
    public void testSqlDiffParseMultiHunk() throws Exception {
        // given
        DiffParser parser = new UnifiedDiffParser();
        InputStream in = getClass().getResourceAsStream("idea_sql_multihunk.diff");

        // when
        List<Diff> diffs = parser.parse(in);

        // then
        Assert.assertNotNull(diffs);
        Assert.assertEquals(1, diffs.size());

        //==============================================================================================================
        Diff diff1 = diffs.get(0);
        Assert.assertEquals(2, diff1.getHunks().size());
        Assert.assertEquals(5, diff1.getHeaderLines().size());

        assertEquals("sql/script.sql", diff1.getFromFileName());
        assertEquals("sql/script.sql", diff1.getToFileName());

        Hunk hunk1_1 = diff1.getHunks().get(0);
        assertEquals(1, hunk1_1.getFromFileRange().getLineStart());
        assertEquals(11, hunk1_1.getFromFileRange().getLineCount());
        assertEquals(1, hunk1_1.getToFileRange().getLineStart());
        assertEquals(11, hunk1_1.getToFileRange().getLineCount());

        List<Line> lines = hunk1_1.getLines();
        assertEquals(13, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals("-- @author someAuthor -- removed line with sql comment", lines.get(3).getContent());
        assertEquals(Line.LineType.FROM, lines.get(4).getLineType());
        assertEquals("-- @issue 1234 -- removed line with sql comment", lines.get(4).getContent());
        assertEquals(Line.LineType.NEUTRAL, lines.get(5).getLineType());
        assertEquals(" -- some sql comment line", lines.get(5).getContent());//TODO should neutral line trim first space char?

        assertEquals(Line.LineType.TO, lines.get(7).getLineType());
        assertEquals("-- some new comments", lines.get(7).getContent());
        assertEquals(Line.LineType.TO, lines.get(8).getLineType());
        assertEquals("-- in sql file", lines.get(8).getContent());
        assertEquals(Line.LineType.TO, lines.get(9).getLineType());
        assertEquals("", lines.get(9).getContent());

        //==============================================================================================================
        Hunk hunk1_2 = diff1.getHunks().get(1);
        assertEquals(22, hunk1_2.getFromFileRange().getLineStart());
        assertEquals(6, hunk1_2.getFromFileRange().getLineCount());
        assertEquals(22, hunk1_2.getToFileRange().getLineStart());
        assertEquals(7, hunk1_2.getToFileRange().getLineCount());

        lines = hunk1_2.getLines();
        assertEquals(6, lines.size());

        assertEquals(Line.LineType.TO, lines.get(3).getLineType());
        assertEquals("        -- added multihunk comment in another place", lines.get(3).getContent());
        //==============================================================================================================
    }

    @Test
    public void testSqlDiffParseMultiFileMultiHunk() throws Exception {
        // given
        DiffParser parser = new UnifiedDiffParser();
        InputStream in = getClass().getResourceAsStream("idea_sql_multifile_multihunk.diff");

        // when
        List<Diff> diffs = parser.parse(in);

        // then
        Assert.assertNotNull(diffs);
        Assert.assertEquals(2, diffs.size());

        //==============================================================================================================
        Diff diff1 = diffs.get(0);
        Assert.assertEquals(1, diff1.getHunks().size());
        Assert.assertEquals(5, diff1.getHeaderLines().size());

        assertEquals("sql/script1.sql", diff1.getFromFileName());
        assertEquals("sql/script1.sql", diff1.getToFileName());

        Hunk hunk1_1 = diff1.getHunks().get(0);
        assertEquals(1, hunk1_1.getFromFileRange().getLineStart());
        assertEquals(7, hunk1_1.getFromFileRange().getLineCount());
        assertEquals(1, hunk1_1.getToFileRange().getLineStart());
        assertEquals(6, hunk1_1.getToFileRange().getLineCount());

        List<Line> lines = hunk1_1.getLines();
        assertEquals(6, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals("-- @author someAuthor -- removed line with sql comment", lines.get(3).getContent());
        assertEquals(Line.LineType.NEUTRAL, lines.get(4).getLineType());
        assertEquals(" -- @issue 12345", lines.get(4).getContent());

        //==============================================================================================================
        Diff diff2 = diffs.get(1);
        Assert.assertEquals(2, diff2.getHunks().size());
        Assert.assertEquals(5, diff2.getHeaderLines().size());

        assertEquals("sql/script2.sql", diff2.getFromFileName());
        assertEquals("sql/script2.sql", diff2.getToFileName());

        //==============================================================================================================
        Hunk hunk2_1 = diff2.getHunks().get(0);
        assertEquals(1, hunk2_1.getFromFileRange().getLineStart());
        assertEquals(11, hunk2_1.getFromFileRange().getLineCount());
        assertEquals(1, hunk2_1.getToFileRange().getLineStart());
        assertEquals(11, hunk2_1.getToFileRange().getLineCount());

        lines = hunk2_1.getLines();
        assertEquals(13, lines.size());
        assertEquals(Line.LineType.FROM, lines.get(3).getLineType());
        assertEquals("-- @author someAuthor", lines.get(3).getContent());
        assertEquals(Line.LineType.FROM, lines.get(4).getLineType());
        assertEquals("-- @issue 112233 -- removed line with sql comment", lines.get(4).getContent());

        assertEquals(Line.LineType.TO, lines.get(7).getLineType());
        assertEquals("-- some new comments", lines.get(7).getContent());
        assertEquals(Line.LineType.TO, lines.get(8).getLineType());
        assertEquals("-- in sql file", lines.get(8).getContent());

        //==============================================================================================================
        Hunk hunk2_2 = diff2.getHunks().get(1);
        assertEquals(22, hunk2_2.getFromFileRange().getLineStart());
        assertEquals(6, hunk2_2.getFromFileRange().getLineCount());
        assertEquals(22, hunk2_2.getToFileRange().getLineStart());
        assertEquals(7, hunk2_2.getToFileRange().getLineCount());

        lines = hunk2_2.getLines();
        assertEquals(6, lines.size());
        assertEquals(Line.LineType.TO, lines.get(3).getLineType());
        assertEquals("        -- added multihunk comment in another place", lines.get(3).getContent());
        assertEquals(Line.LineType.NEUTRAL, lines.get(4).getLineType());
        assertEquals("   BEGIN", lines.get(4).getContent());
        //==============================================================================================================
    }

}
