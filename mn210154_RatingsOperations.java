package student;

import rs.ac.bg.etf.sab.operations.RatingsOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mn210154_RatingsOperations implements RatingsOperations{

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public boolean addRating(Integer userId, Integer movieId, Integer score) {

        String query = "insert into Rating (IdU, IdM, Score) values (?, ?, ?)";

        String rewardUser =  "{call SP_REWARD_USER_(?, ?)}";

        PreparedStatement ps = null;
        CallableStatement cs = null;
        boolean result = false;

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, movieId);
            ps.setInt(3, score);

            int res = ps.executeUpdate();

            if (res == 1) {
                cs = connection.prepareCall(rewardUser);
                cs.setInt(1, userId);
                cs.setInt(2, movieId);
                cs.execute();

                connection.commit();
                result = true;
            } else {
                connection.rollback();
            }

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
                if (cs != null) cs.close();
                if (ps != null) ps.close();

                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public boolean updateRating(Integer userId, Integer movieId, Integer newScore) {

        String query = "update rating\n" +
                "set DateModified = SYSDATETIME(), Score = ?\n" +
                "where IdU = ? and IdM = ?";

        boolean result = false;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, newScore);
            ps.setInt(2, userId);
            ps.setInt(3, movieId);

            int res = ps.executeUpdate();

            if (res == 1) result = true;

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean removeRating(Integer userId, Integer movieId) {

        String query = "delete from Rating\n" +
                "where IdU = ? and IdM = ?" ;

        boolean result = false;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            int res = ps.executeUpdate();

            if (res == 1) result = true;

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getRating(Integer userId, Integer movieId) {

        Integer score = null;

        String query = "select Score\n" +
                "from Rating\n" +
                "where IdU = ? and IdM = ?" ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, movieId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) score = rs.getInt(1);

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return score;
    }

    @Override
    public List<Integer> getRatedMoviesByUser(Integer userId) {

        List<Integer> movieIds = new ArrayList<>();

        String query = "select IdM\n" +
                "from Rating\n" +
                "where IdU = ?\n" +
                "order by IdM" ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
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
    public List<Integer> getUsersWhoRatedMovie(Integer movieId) {

        List<Integer> userIds = new ArrayList<>();

        String query = "select IdU\n" +
                "from Rating\n" +
                "where IdM = ?\n" +
                "order by IdU" ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                userIds.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIds;
    }
}
