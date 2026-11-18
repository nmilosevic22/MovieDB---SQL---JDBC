package student;

import rs.ac.bg.etf.sab.operations.GenresOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mn210154_GenresOperations implements GenresOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addGenre(String s) {
        String query = "INSERT INTO Genre (GenreName) Values (?)" ;

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s);
            int res = ps.executeUpdate();

            if(res == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) result = rs.getInt(1);
                }
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer updateGenre(Integer integer, String s) {
        String query = "update Genre\n" +
                "set GenreName = ?\n" +
                "where IdG = ?" ;

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, s);
            ps.setInt(2, integer);

            int res = ps.executeUpdate();

            if(res == 1) {
                result = integer;
            }

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer removeGenre(Integer integer) {
        String query = "delete from Genre\n" +
                "where IdG = ?" ;

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, integer);

            int res = ps.executeUpdate();

            if (res == 1) {
                result = integer;
            }

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean doesGenreExist(String s) {

        String query = "select 1\n" +
                "from Genre\n" +
                "where GenreName = ?";

        boolean result = false;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, s);

            try (ResultSet rs = ps.executeQuery()) {
                result = rs.next();
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getGenreId(String s) {

        String query = "select IdG\n" +
                "from Genre\n" +
                "where GenreName = ?";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, s);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    result = rs.getInt(1);
                }
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Integer> getAllGenreIds() {

        List<Integer> genreIds = new ArrayList<>();

        String query = "select IdG\n" +
                "from Genre\n" +
                "order by IdG" ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                genreIds.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genreIds;
    }
}
