package student;

import rs.ac.bg.etf.sab.operations.MoviesOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mn210154_MoviesOperations implements MoviesOperations{

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addMovie(String s, Integer integer, String s1) {

        String query1 = "insert into Movie (Title, Director)\n" +
                "values (?, ?)" ;


        String query2 = "insert into MovieGenre (IdM, IdG) values (?, ?)";
        Integer movieId = null;
        ResultSet generatedKeys = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s);
            ps.setString(2, s1);

            int insertedMovie = ps.executeUpdate();

            if(insertedMovie != 1) {
                connection.rollback();
                return null;
            }

            generatedKeys = ps.getGeneratedKeys();

            if(!generatedKeys.next()) {
                connection.rollback();
                return null;
            }

            movieId = generatedKeys.getInt(1);

            ps2 = connection.prepareStatement(query2);
            ps2.setInt(1, movieId);
            ps2.setInt(2, integer);

            int insertedGenre = ps2.executeUpdate();

            if (insertedGenre != 1) {
                connection.rollback();
                return null;
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
                if (generatedKeys != null) generatedKeys.close();

                if (ps2 != null) ps2.close();

                if(ps != null) ps.close();

                connection.setAutoCommit(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return movieId;
    }

    @Override
    public Integer updateMovieTitle(Integer integer, String s) {

        String query = "update Movie\n" +
                "set Title = ?\n" +
                "where IdM = ?" ;

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
    public Integer addGenreToMovie(Integer integer, Integer integer1) {

        String query = "insert into MovieGenre (IdM, IdG) values (?, ?)";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, integer);
            ps.setInt(2, integer1);

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
    public Integer removeGenreFromMovie(Integer integer, Integer integer1) {

        String query = "delete from MovieGenre\n" +
                "where IdM = ? and IdG = ?";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, integer);
            ps.setInt(2, integer1);

            int res = ps.executeUpdate();

            if (res == 1) {
                result = integer;
            }

            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Integer updateMovieDirector(Integer id, String newDirector) {

        String query = "update Movie\n" +
                "set Director = ?\n" +
                "where IdM = ?";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newDirector);
            ps.setInt(2, id);

            int res = ps.executeUpdate();

            if (res == 1) {
                result = id;
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer removeMovie(Integer id) {

        String deleteGenres = "delete from MovieGenre where IdM = ?";
        String deleteTags = "delete from MovieTag where IdM = ?";
        String query = "delete from Movie where IdM = ?" ;

        Integer result = null;

        try {
            connection.setAutoCommit(false);

            PreparedStatement psGenres = connection.prepareStatement(deleteGenres);
            psGenres.setInt(1, id);
            psGenres.executeUpdate();

            PreparedStatement psTags = connection.prepareStatement(deleteTags);
            psTags.setInt(1, id);
            psGenres.executeUpdate();

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            int res = ps.executeUpdate();

            if (res == 1) {
                connection.commit();
                result = id;
            } else {
                connection.rollback();
            }

            psGenres.close();
            psTags.close();
            ps.close();

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

        return result;
    }

    @Override
    public List<Integer> getMovieIds(String title, String director) {

        List<Integer> movieIds = new ArrayList<>();

        String query = "select IdM\n" +
                "from Movie\n" +
                "where Title = ? and Director = ?\n" +
                "order by IdM";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, director);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                movieIds.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieIds;

    }

    @Override
    public List<Integer> getAllMovieIds() {

        List<Integer> movieIds = new ArrayList<>();

        String query = "select IdM\n" +
                "from Movie\n" +
                "order by IdM" ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                movieIds.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieIds;
    }

    @Override
    public List<Integer> getMovieIdsByGenre(Integer genreId) {

        List<Integer> movieIds = new ArrayList<>();

        String query = "select IdM\n" +
                "from MovieGenre\n" +
                "where IdG = ?\n" +
                "order by IdM";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, genreId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                movieIds.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieIds;
    }

    @Override
    public List<Integer> getGenreIdsForMovie(Integer movieId) {

        List<Integer> genreIds = new ArrayList<>();

        String query = "select IdG\n" +
                "from MovieGenre\n" +
                "where IdM = ?\n" +
                "order by IdG" ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, movieId);
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

    @Override
    public List<Integer> getMovieIdsByDirector(String director) {

        List<Integer> movieIds = new ArrayList<>();

        String query = "select IdM \n" +
                "from Movie\n" +
                "where Director = ?\n" +
                "order by IdM";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, director);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                movieIds.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieIds;
    }

    @Override
    public String getMovieTrend(Integer movieId) {

        String query = "select MovieStatus\n" +
                "from Movie\n" +
                "where IdM = ?" ;

        String result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) result = rs.getString(1);

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
