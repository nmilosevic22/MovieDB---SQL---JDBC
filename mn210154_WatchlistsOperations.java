package student;

import rs.ac.bg.etf.sab.operations.WatchlistsOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class mn210154_WatchlistsOperations implements WatchlistsOperations{

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public boolean addMovieToWatchlist(Integer userId, Integer movieId) {

        String query = "insert into WatchList (IdU, IdM) values (?, ?)";

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
    public boolean removeMovieFromWatchlist(Integer userId, Integer movieId) {

        String query = "delete from WatchList where IdU = ? and IdM = ?";

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
    public boolean isMovieInWatchlist(Integer userId, Integer movieId) {

        String query = "select 1\n" +
                "from WatchList\n" +
                "where IdU = ? and IdM = ?";

        boolean result = false;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, movieId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) result = true;

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Integer> getMoviesInWatchlist(Integer userId) {

        List<Integer> moviesId = new ArrayList<>();

        String query = "select IdM\n" +
                "from WatchList\n" +
                "where IdU = ?\n" +
                "order by IdM";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                moviesId.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moviesId;
    }

    @Override
    public List<Integer> getUsersWithMovieInWatchlist(Integer movieId) {

        String query = "select IdU\n" +
                "from WatchList\n" +
                "where IdM = ?\n" +
                "order by IdU";

        List<Integer> usersId = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, movieId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usersId.add(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersId;
    }
}
