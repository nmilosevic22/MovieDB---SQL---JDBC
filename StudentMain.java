package student;

import rs.ac.bg.etf.sab.operations.*;
import rs.ac.bg.etf.sab.tests.TestHandler;
import rs.ac.bg.etf.sab.tests.TestRunner;

public class StudentMain {
    public static void main(String[] args) throws Exception {
// Uncomment and change fallowing lines
        GeneralOperations generalOperations = new mn210154_GeneralOperations();
        GenresOperations genresOperations = new mn210154_GenresOperations();
        MoviesOperations moviesOperations = new mn210154_MoviesOperations();
        RatingsOperations ratingsOperation = new mn210154_RatingsOperations();
        TagsOperations tagsOperations = new mn210154_TagsOperations();
        UsersOperations usersOperations = new mn210154_UsersOperations();
        WatchlistsOperations watchlistsOperations = new mn210154_WatchlistsOperations();

        TestHandler.createInstance(
                genresOperations,
                moviesOperations,
                ratingsOperation,
                tagsOperations,
                usersOperations,
                watchlistsOperations,
                generalOperations);
        TestRunner.runTests();
    }
}