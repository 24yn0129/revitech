package com.example.revitech.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {
    private final Connection conn;

    public ChatDAO(Connection conn) {
        this.conn = conn;
    }

    // チャットルーム作成
    public long createChatRoom(String name, String type, long creatorUserId) throws SQLException {
        String sql = "INSERT INTO chat_rooms (name, type, creatar_user_id, created_at) VALUES (?, ?, ?, GETDATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setLong(3, creatorUserId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Failed to retrieve chat_room ID.");
                }
            }
        }
    }

    // チャットメンバー追加（重複チェックあり）
    public boolean addChatMember(long roomId, long userId) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM chat_members WHERE room_id = ? AND user_id = ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setLong(1, roomId);
            checkPs.setLong(2, userId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;  // 既に存在
                }
            }
        }

        String insertSql = "INSERT INTO chat_members (room_id, user_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setLong(1, roomId);
            ps.setLong(2, userId);
            ps.executeUpdate();
            return true;
        }
    }

    // メッセージ送信
    public long sendMessage(long roomId, long senderUserId, String body) throws SQLException {
        String sql = "INSERT INTO chat_messages (room_id, sender_user_id, body, created_at) VALUES (?, ?, ?, GETDATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, roomId);
            ps.setLong(2, senderUserId);
            ps.setString(3, body);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Failed to retrieve chat_message ID.");
                }
            }
        }
    }

    // チャットルームのメンバー一覧取得
    public List<Long> getChatMembers(long roomId) throws SQLException {
        List<Long> members = new ArrayList<>();
        String sql = "SELECT user_id FROM chat_members WHERE room_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(rs.getLong("user_id"));
                }
            }
        }
        return members;
    }
}
