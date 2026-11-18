package student;

import rs.ac.bg.etf.sab.operations.UsersOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mn210154_UsersOperations implements UsersOperations{

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addUser(String username) {

        String query = "insert into MovieUser (Username) values (?)";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);

            int res = ps.executeUpdate();

            if (res == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()) result = rs.getInt(1);
                }
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer updateUser(Integer id, String newUsername) {

        String query = "update MovieUser\n" +
                "set Username = ?\n" +
                "where Id = ?" ;

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newUsername);
            ps.setInt(2, id);

            int res = ps.executeUpdate();

            if (res == 1) result = id;

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer removeUser(Integer id) {

        String query = "delete from MovieUser where Id = ?";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            int res = ps.executeUpdate();

            if(res == 1) result = id;

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean doesUserExist(String username) {

        String query = "select 1\n" +
                "from MovieUser\n" +
                "where Username = ?" ;

        boolean result = false;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            result = rs.next();

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Integer getUserId(String username) {

        String query = "select Id\n" +
                "from MovieUser\n" +
                "where Username = ?";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) result = rs.getInt(1);

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Integer> getAllUserIds() {

        String query = "select Id\n" +
                "from MovieUser\n" +
                "order by Id";

        List<Integer> userIds = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement(query);
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

    @Override
    public List<Integer> getRecommendedMoviesFromFavoriteGenres(Integer userId) {

        List<Integer> movieIds = new ArrayList<>();

        String query = "{call SP_GET_RECOMMENDATIONS(?)}";

        try {
            CallableStatement cs = connection.prepareCall(query);
            cs.setInt(1, userId);

            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                movieIds.add(rs.getInt("IdM"));
            }

            rs.close();
            cs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieIds;
    }

    @Override
    public Integer getRewards(Integer userId) {

        String query = "select RewardCount\n" +
                "from MovieUser\n" +
                "where Id = ?";

        Integer result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) result = rs.getInt(1);

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<String> getThematicSpecializations(Integer userId) {

        List<String> tags = new ArrayList<>();

        String query = "select TagName\n" +
                "from UserSpecialization\n" +
                "where IdU = ?\n" +
                "order by TagName" ;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tags.add(rs.getString(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }

    @Override
    public String getUserDescription(Integer userId) {

        String query = "select UserDescription \n" +
                "from UserDescription\n" +
                "where Id = ?" ;

        String result = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);

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
