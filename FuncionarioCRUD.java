// Arquivo: FuncionarioCRUD.java
import java.sql.*;
import java.util.Scanner;
import java.text.ParseException;

public class FuncionarioCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public FuncionarioCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== FUNCIONÁRIOS ===");
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
        java.sql.Date dataNascimento = null;
        java.sql.Date dataAdmissao = null;
        java.sql.Date dataDemissao = null;

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        while (dataNascimento == null) {
            System.out.print("Data de nascimento (dd/MM/yyyy): ");
            String dataNascimentoStr = scanner.nextLine();
            try {
                dataNascimento = parseDate(dataNascimentoStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy. Exemplo: 25/12/1990");
            }
        }

        while (dataAdmissao == null) {
            System.out.print("Data de adesão (dd/MM/yyyy): ");
            String dataAdmissaoStr = scanner.nextLine();
            try {
                dataAdmissao = parseDate(dataAdmissaoStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            }
        }

        System.out.print("Data de demissão (dd/MM/yyyy) [deixe em branco se não houver]: ");
        String dataDemissaoStr = scanner.nextLine();
        if (!dataDemissaoStr.trim().isEmpty()) {
            try {
                dataDemissao = parseDate(dataDemissaoStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Usando null para data de demissão.");
            }
        }

        System.out.print("Status: ");
        int status = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Cargo (ID): ");
        int idCargo = scanner.nextInt();
        scanner.nextLine();

        int idTelefone = -1;
        String sqlInsertTelefone = "INSERT INTO telefones (numero) VALUES (?)";
        try (PreparedStatement stmtTelefone = connection.prepareStatement(sqlInsertTelefone, Statement.RETURN_GENERATED_KEYS)) {
            stmtTelefone.setString(1, telefone);
            stmtTelefone.executeUpdate();

            try (ResultSet generatedKeys = stmtTelefone.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idTelefone = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Falha ao obter ID do telefone.");
                }
            }
        }

        String sqlInsertFuncionario = "INSERT INTO funcionarios (nome, data_nascimento, data_admissao, data_demissao, status, id_telefone, id_cargo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmtFuncionario = connection.prepareStatement(sqlInsertFuncionario)) {
            stmtFuncionario.setString(1, nome);
            stmtFuncionario.setDate(2, dataNascimento);
            stmtFuncionario.setDate(3, dataAdmissao);
            if (dataDemissao != null) {
                stmtFuncionario.setDate(4, dataDemissao);
            } else {
                stmtFuncionario.setNull(4, Types.DATE);
            }
            stmtFuncionario.setInt(5, status);
            stmtFuncionario.setInt(6, idTelefone);
            stmtFuncionario.setInt(7, idCargo);

            stmtFuncionario.executeUpdate();
            System.out.println("Funcionário cadastrado com sucesso!");
        }
    }
    
    private void listar() throws SQLException {
        String sql = "SELECT f.*, t.numero as telefone, c.nome as cargo_nome " +
                     "FROM funcionarios f " +
                     "INNER JOIN telefones t ON f.id_telefone = t.id_telefone " +
                     "INNER JOIN cargos c ON f.id_cargo = c.id_cargo";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Nascimento: %s | Adesão: %s | Demissão: %s | Status: %d | Telefone: %s | Cargo: %s\n",
                    rs.getInt("id_funcionario"),
                    rs.getString("nome"),
                    rs.getDate("data_nascimento"),
                    rs.getDate("data_admissao"),
                    rs.getDate("data_demissao") != null ? rs.getDate("data_demissao") : "N/A",
                    rs.getInt("status"),
                    rs.getString("telefone"),
                    rs.getString("cargo_nome"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do funcionário a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        java.sql.Date dataNascimento = null;
        while (dataNascimento == null) {
            System.out.print("Nova data de nascimento (dd/MM/yyyy): ");
            String dataNascimentoStr = scanner.nextLine();
            try {
                dataNascimento = parseDate(dataNascimentoStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            }
        }

        java.sql.Date dataAdmissao = null;
        while (dataAdmissao == null) {
            System.out.print("Nova data de adesão (dd/MM/yyyy): ");
            String dataAdmissaoStr = scanner.nextLine();
            try {
                dataAdmissao = parseDate(dataAdmissaoStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            }
        }

        java.sql.Date dataDemissao = null;
        System.out.print("Nova data de demissão (dd/MM/yyyy) [deixe em branco se não houver]: ");
        String dataDemissaoStr = scanner.nextLine();
        if (!dataDemissaoStr.trim().isEmpty()) {
            try {
                dataDemissao = parseDate(dataDemissaoStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Usando null para data de demissão.");
            }
        }

        System.out.print("Novo status: ");
        int status = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        int idTelefone = -1;
        String sqlInsertTelefone = "INSERT INTO telefones (numero) VALUES (?)";
        try (PreparedStatement stmtTelefone = connection.prepareStatement(sqlInsertTelefone, Statement.RETURN_GENERATED_KEYS)) {
            stmtTelefone.setString(1, telefone);
            stmtTelefone.executeUpdate();

            try (ResultSet generatedKeys = stmtTelefone.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idTelefone = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Erro ao cadastrar, nenhum ID obtido.");
                }
            }
        }

        System.out.print("Cargo (ID): ");
        int idCargo = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE funcionarios SET nome = ?, data_nascimento = ?, data_admissao = ?, data_demissao = ?, status = ?, id_telefone = ?, id_cargo = ? WHERE id_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setDate(2, dataNascimento);
            stmt.setDate(3, dataAdmissao);
            if (dataDemissao != null) {
                stmt.setDate(4, dataDemissao);
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setInt(5, status);
            stmt.setInt(6, idTelefone);
            stmt.setInt(7, idCargo);
            stmt.setInt(8, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Funcionário atualizado com sucesso!");
            } else {
                System.out.println("Funcionário não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID do funcionário a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM funcionarios WHERE id_funcionario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Funcionário deletado com sucesso!");
            } else {
                System.out.println("Funcionário não encontrado!");
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