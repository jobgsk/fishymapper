package com.technomark.fishymapper;

import com.technomark.fishymapper.parser.ParserDeleteTest;
import com.technomark.fishymapper.parser.ParserInsertTest;
import com.technomark.fishymapper.parser.ParserUpdateTest;
import com.technomark.fishymapper.parser.SelectBuilderTest;
import com.technomark.fishymapper.persist.DeleteCascadeTest;
import com.technomark.fishymapper.persist.PersistMergeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by troy on 7/31/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ParserInsertTest.class,
        ParserUpdateTest.class,
        ParserDeleteTest.class,
        SelectBuilderTest.class,
        PersistMergeTest.class,
        DeleteCascadeTest.class
})
public class AllTests {
}
