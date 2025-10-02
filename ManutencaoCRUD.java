// Arquivo: ManutencaoCRUD.java
import java.sql.*;
import java.text.ParseException;
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
        scanner.nextLine();

        System.out.print("Status: ");
        int status = scanner.nextInt();
        scanner.nextLine();

        java.sql.Date dataPrevista = null;
        while (dataPrevista == null) {
            System.out.print("Data prevista (dd/MM/yyyy): ");
            String dataPrevistaStr = scanner.nextLine();
            try {
                dataPrevista = parseDate(dataPrevistaStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy. Exemplo: 25/12/1990");
            }
        }

        java.sql.Date dataRealizada = null;
        System.out.print("Data realizada (dd/MM/yyyy) [deixe em branco se não houver]: ");
        String dataRealizadaStr = scanner.nextLine();
        if (!dataRealizadaStr.trim().isEmpty()) {
            try {
                dataRealizada = parseDate(dataRealizadaStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Usando null para data realizada.");
            }
        }

        System.out.print("Equipamento (ID): ");
        int idEquipamento = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Funcionário (ID): ");
        int idFuncionario = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO manutencoes (custo, status, data_prevista, data_realizada, id_equipamento, id_funcionario) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setFloat(1, custo);
            stmt.setInt(2, status);
            stmt.setDate(3, dataPrevista);
            if (dataRealizada != null) {
                stmt.setDate(4, dataRealizada);
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, idEquipamento);
            stmt.setInt(6, idFuncionario);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Manutenção cadastrada com sucesso!");
            }
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT * FROM manutencoes";
        
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.printf("ID: %d | Custo: R$%.2f | Status: %d | Data prevista: %s | Data realizada: %s | Equipamento (ID): %d | Funcionário (ID): %d\n",
                    rs.getInt("id_manutencao"),
                    rs.getFloat("custo"),
                    rs.getInt("status"),
                    rs.getDate("data_prevista"),
                    rs.getDate("data_realizada") != null ? rs.getDate("data_realizada") : "N/A",
                    rs.getInt("id_equipamento"),
                    rs.getInt("id_funcionario"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID da manutenção a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Novo custo: ");
        float custo = scanner.nextFloat();
        scanner.nextLine();

        System.out.print("Novo status: ");
        int status = scanner.nextInt();
        scanner.nextLine();

        java.sql.Date dataPrevista = null;
        while (dataPrevista == null) {
            System.out.print("Nova data prevista (dd/MM/yyyy): ");
            String dataPrevistaStr = scanner.nextLine();
            try {
                dataPrevista = parseDate(dataPrevistaStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            }
        }

        java.sql.Date dataRealizada = null;
        System.out.print("Nova data realizada (dd/MM/yyyy) [deixe em branco se não houver]: ");
        String dataRealizadaStr = scanner.nextLine();
        if (!dataRealizadaStr.trim().isEmpty()) {
            try {
                dataRealizada = parseDate(dataRealizadaStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Usando null para data realizada.");
            }
        }

        System.out.print("Equipamento (ID): ");
        int idEquipamento = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Funcionário (ID): ");
        int idFuncionario = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE manutencoes SET custo = ?, status = ?, data_prevista = ?, data_realizada = ?, id_equipamento = ?, id_funcionario = ? WHERE id_manutencao = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setFloat(1, custo);
            stmt.setInt(2, status);
            stmt.setDate(3, dataPrevista);
            if (dataRealizada != null) {
                stmt.setDate(4, dataRealizada);
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, idEquipamento);
            stmt.setInt(6, idFuncionario);
            stmt.setInt(7, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Manutenção atualizada com sucesso!");
            } else {
                System.out.println("Manutenção não encontrada!");
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

    // helper de data
    private java.sql.Date parseDate(String dateString) throws ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        java.util.Date parsedUtilDate = sdf.parse(dateString);

        return new java.sql.Date(parsedUtilDate.getTime());
    }
}