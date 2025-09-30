// Arquivo: EquipamentoCRUD.java
import java.sql.*;
import java.util.Scanner;

public class EquipamentoCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public EquipamentoCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== PLANOS ===");
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
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Valor: ");
        float valor = scanner.nextFloat();
        System.out.print("Status: ");
        int status = scanner.nextInt();
        
        String sql = "INSERT INTO equipamentos (modelo, valor, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, modelo);
            stmt.setFloat(2, valor);
            stmt.setInt(3, status);
            stmt.executeUpdate();

            System.out.println("Equipamento cadastrado com sucesso!");
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT * FROM equipamentos";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("ID: %d | Modelo: %s | Valor: %.2f | Status: %d\n",
                    rs.getInt("id_equipamento"),
                    rs.getString("modelo"),
                    rs.getFloat("valor"),
                    rs.getInt("status"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do equipamento a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Novo modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Novo valor: ");
        float valor = scanner.nextFloat();
        System.out.print("Novo status: ");
        int status = scanner.nextInt();
        
        String sql = "UPDATE equipamentos SET modelo = ?, valor = ?, status = ? WHERE id_equipamento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, modelo);
            stmt.setFloat(2, valor);
            stmt.setInt(3, status);
            stmt.setInt(6, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Equipamento atualizado com sucesso!");
            } else {
                System.out.println("Equipamento não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID do equipamento a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM equipamentos WHERE id_equipamento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Equipamento deletado com sucesso!");
            } else {
                System.out.println("Equipamento não encontrado!");
            }
        }
    }
}