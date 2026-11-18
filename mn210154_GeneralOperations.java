package student;

import rs.ac.bg.etf.sab.operations.GeneralOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class mn210154_GeneralOperations implements GeneralOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public void eraseAll() {
        String[] queries = {
                "delete from WatchList",
                "delete from Rating",
                "delete from MovieTag",
                "delete from MovieGenre",
                "delete from Tag",
                "delete from Genre",
                "delete from Movie",
                "delete from MovieUser",

                "dbcc checkident ('MovieUser' , RESEED, 0)",
                "dbcc checkident ('Movie' , RESEED, 0)",
                "dbcc checkident ('Genre' , RESEED, 0)",
                "dbcc checkident ('Tag' , RESEED, 0)"
        };

        try {
            connection.setAutoCommit(false);

            for (String query : queries) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.execute();
                ps.close();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
