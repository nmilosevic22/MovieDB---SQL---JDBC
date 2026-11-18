package student;

import rs.ac.bg.etf.sab.operations.TagsOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mn210154_TagsOperations implements TagsOperations{

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public Integer addTag(Integer movieId, String tag) {

        String getTagQuery =
                "SELECT IdT FROM Tag WHERE TagName = ?";

        String insertTagQuery =
                "INSERT INTO Tag (TagName) VALUES (?)";

        String insertMovieTagQuery =
                "INSERT INTO MovieTag (IdM, IdT) VALUES (?, ?)";

        PreparedStatement psGetTag = null;
        PreparedStatement psInsertTag = null;
        PreparedStatement psInsertMovieTag = null;
        ResultSet rs = null;

        Integer result = null;

        try {
            connection.setAutoCommit(false);

            Integer tagId = null;

            psGetTag = connection.prepareStatement(getTagQuery);
            psGetTag.setString(1, tag);

            rs = psGetTag.executeQuery();

            if (rs.next()) {
                tagId = rs.getInt("IdT");
            }

            rs.close();
            rs = null;

            if (tagId == null) {
                psInsertTag = connection.prepareStatement(
                        insertTagQuery,
                        Statement.RETURN_GENERATED_KEYS
                );

                psInsertTag.setString(1, tag);

                int insertedTags = psInsertTag.executeUpdate();

                if (insertedTags != 1) {
                    connection.rollback();
                    return null;
                }

                rs = psInsertTag.getGeneratedKeys();

                if (rs.next()) {
                    tagId = rs.getInt(1);
                } else {
                    connection.rollback();
                    return null;
                }

                rs.close();
                rs = null;
            }

            psInsertMovieTag = connection.prepareStatement(insertMovieTagQuery);
            psInsertMovieTag.setInt(1, movieId);
            psInsertMovieTag.setInt(2, tagId);

            int insertedRelations = psInsertMovieTag.executeUpdate();

            if (insertedRelations == 1) {
                connection.commit();
                result = movieId;
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {
            try {
                if (rs != null) rs.close();
                if (psGetTag != null) psGetTag.close();
                if (psInsertTag != null) psInsertTag.close();
                if (psInsertMovieTag != null) psInsertMovieTag.close();

                connection.setAutoCommit(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public Integer removeTag(Integer movieId, String tag) {

        String query = "select IdT\n" +
                "from Tag\n" +
                "where TagName = ?" ;

        String query1 = "delete from MovieTag where IdM = ? and IdT = ?";

        Integer result = null;
        Integer tagId = null;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, tag);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) tagId = rs.getInt(1);

            if (tagId != null) {
                PreparedStatement ps2 = connection.prepareStatement(query1);
                ps2.setInt(1, movieId);
                ps2.setInt(2, tagId);

                int res = ps2.executeUpdate();

                if(res == 1) {
                    result = movieId;
                }

                ps2.close();
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int removeAllTagsForMovie(Integer movieId) {

        String query = "delete from MovieTag where IdM = ?";

        int result = 0;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, movieId);

            result = ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean hasTag(Integer movieId, String tag) {

        String query = "select 1\n" +
                "from MovieTag mt join Tag t on mt.IdT = t.IdT\n" +
                "where mt.IdM = ? and t.TagName = ?" ;

        boolean result = false;

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, movieId);
            ps.setString(2, tag);

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
    public List<String> getTagsForMovie(Integer movieId) {

        List<String> tags = new ArrayList<>();

        String query = "select t.TagName\n" +
                "from MovieTag mt join Tag t on mt.IdT = t.IdT\n" +
                "where mt.IdM = ?\n" +
                "order by t.TagName";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, movieId);

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
    public List<Integer> getMovieIdsByTag(String tag) {

        List<Integer> movieIds = new ArrayList<>();

        String query = "select mt.IdM\n" +
                "from MovieTag mt join Tag t on mt.IdT = t.IdT\n" +
                "where t.TagName = ?\n" +
                "order by mt.IdM";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, tag);
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
    public List<String> getAllTags() {

        List<String> tags = new ArrayList<>();

        String query = "select TagName\n" +
                "from Tag\n" +
                "order by TagName";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
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
}
