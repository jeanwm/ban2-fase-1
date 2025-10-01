// Arquivo: ManutencaoCRUD.java
import java.sql.*;
import java.util.Scanner;

public class ManutencaoCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public ManutencaoCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== MANUTENÇÕES ===");
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar");
            System.out.println("3. Atualizar");
            System.out.println("4. Deletar");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> deletar();
                case 5 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrar() throws SQLException {
        System.out.print("Custo: ");
        float custo = scanner.nextFloat();
        System.out.print("Status: ");
        int status = scanner.nextInt();
        System.out.print("Data prevista: ");
        String data_prevista = scanner.nextLine();
        System.out.print("Data realizada: ");
        String data_realizada = scanner.nextLine();
        System.out.print("Equipamento: ");
        int id_equipamento = scanner.nextInt();
        
        String sql = "INSERT INTO manutencoes (custo, status, data_prevista, data_realizada, id_equipamento) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setFloat(1, custo);
            stmt.setInt(2, status);
            stmt.setString(3, data_prevista);
            stmt.setString(4, data_realizada);
            stmt.setInt(5, id_equipamento);
            stmt.executeUpdate();

            System.out.println("Manutenção cadastrada com sucesso!");
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT * FROM manutencoes";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("ID: %d | Custo: %s | Status: %d | Data prevista: %s | Data realizada: %f | Equipamento: %s\n",
                    rs.getInt("id_manutencao"),
                    rs.getFloat("custo"),
                    rs.getInt("status"),
                    rs.getString("data_prevista"),
                    rs.getString("data_realizada"),
                    rs.getInt("id_equipamento"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do plano a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Novo custo: ");
        float custo = scanner.nextFloat();
        System.out.print("Novo status: ");
        int status = scanner.nextInt();
        System.out.print("Nova data prevista: ");
        String data_prevista = scanner.nextLine();
        System.out.print("Nova data realizada: ");
        String data_realizada = scanner.nextLine();
        // System.out.print("Equipamento: ");
        // int id_equipamento = scanner.nextInt();
        
        String sql = "UPDATE manutencoes SET custo = ?, status = ?, data_prevista = ?, data_realizada = ? WHERE id_manutencao = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setFloat(1, custo);
            stmt.setInt(2, status);
            stmt.setString(3, data_prevista);
            stmt.setString(4, data_realizada);
            stmt.setInt(5, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Manutenção atualizada com sucesso!");
            } else {
                System.out.println("Manutenção não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID da manutenção a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM manutencoes WHERE id_manutencao = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Manutenção deletada com sucesso!");
            } else {
                System.out.println("Manutenção não encontrado!");
            }
        }
    }
}