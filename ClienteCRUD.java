// Arquivo: ClienteCRUD.java
import java.sql.*;
import java.util.Scanner;

public class ClienteCRUD {
    private Connection connection;
    private Scanner scanner;
    
    public ClienteCRUD(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    
    public void menu() throws SQLException {
        while (true) {
            System.out.println("\n=== CLIENTES ===");
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar");
            System.out.println("3. Atualizar");
            System.out.println("4. Deletar");
            System.out.println("5. Vincular Plano");
            System.out.println("6. Voltar");
            System.out.print("Escolha: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> deletar();
                case 5 -> vincular();
                case 6 -> { return; }
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    
    private void cadastrar() throws SQLException {
        java.sql.Date data_nascimento = null;
        java.sql.Date data_adesao = null;

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        while (data_nascimento == null) {
            System.out.print("Data de nascimento (dd/mm/aaaa): ");
            String dataNascimentoStr = scanner.nextLine();
            try {
                data_nascimento = parseDate(dataNascimentoStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
            }
        }

        while (data_adesao == null) {
            System.out.print("Data de adesão (dd/mm/aaaa): ");
            String dataAdesaoStr = scanner.nextLine();
            try {
                data_adesao = parseDate(dataAdesaoStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
            }
        }

        System.out.print("Status: ");
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
        
        String sqlInsertCliente = "INSERT INTO clientes (nome, data_nascimento, data_adesao, status, id_telefone) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmtCliente = connection.prepareStatement(sqlInsertCliente)) {
            stmtCliente.setString(1, nome);
            stmtCliente.setDate(2, data_nascimento);
            stmtCliente.setDate(3, data_adesao);
            stmtCliente.setInt(4, status);
            stmtCliente.setInt(5, idTelefone);
            stmtCliente.executeUpdate();
            System.out.println("Cliente cadastrado com sucesso!");
        }
    }
     
    private void listar() throws SQLException {
        String sql = "SELECT clientes.*, telefones.*, planos.nome as plano_nome FROM clientes LEFT JOIN telefones ON telefones.id_telefone = clientes.id_telefone LEFT JOIN planos ON planos.id_plano = clientes.id_plano";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Data de nascimento: %s | Data de adesão: %s | Status: %d | Telefone: %s | Plano: %s\n",
                    rs.getInt("id_cliente"),
                    rs.getString("nome"),
                    rs.getString("data_nascimento"),
                    rs.getString("data_adesao"),
                    rs.getInt("status"),
                    rs.getString("numero"),
                    rs.getString("plano_nome"));
            }
        }
    }
    
    private void atualizar() throws SQLException {
        System.out.print("ID do cliente a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        
        java.sql.Date data_nascimento = null;
        while (data_nascimento == null) {
            System.out.print("Nova data de nascimento (dd/mm/aaaa): ");
            String dataNascimentoStr = scanner.nextLine();
            try {
                data_nascimento = parseDate(dataNascimentoStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
            }
        }
        
        java.sql.Date data_adesao = null;
        while (data_adesao == null) {
            System.out.print("Nova data de adesão (dd/mm/aaaa): ");
            String dataAdesaoStr = scanner.nextLine();
            try {
                data_adesao = parseDate(dataAdesaoStr);
            } catch (java.text.ParseException e) {
                System.out.println("Formato de data inválido. Use dd/mm/aaaa.");
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

        System.out.print("Novo plano (ID): ");
        int idPlano = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "UPDATE clientes SET nome = ?, data_nascimento = ?, data_adesao = ?, status = ?, id_telefone = ?, id_plano = ? WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setDate(2, data_nascimento);
            stmt.setDate(3, data_adesao);
            stmt.setInt(4, status);
            stmt.setInt(5, idTelefone);
            stmt.setInt(6, idPlano);
            stmt.setInt(7, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Cliente não encontrado!");
            }
        }
    }
    
    private void deletar() throws SQLException {
        System.out.print("ID do cliente a deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Cliente deletado com sucesso!");
            } else {
                System.out.println("Cliente não encontrado!");
            }
        }
    }

    private void vincular() throws SQLException {
        String sqlClientes = "SELECT * FROM clientes";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlClientes)) {
            System.out.println("Clientes:");

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s\n",
                    rs.getInt("id_cliente"),
                    rs.getString("nome"));
            }
        }

        String sqlPlanos = "SELECT * FROM planos";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlPlanos)) {
            System.out.println("Planos:");

            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Descrição: %s | Valor: %.2f\n",
                    rs.getInt("id_plano"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getFloat("valor"));
            }
        }

        System.out.print("ID do cliente: ");
        int id_cliente = scanner.nextInt();
        System.out.print("ID do plano: ");
        int id_plano = scanner.nextInt();
        
        String sql = "UPDATE clientes SET id_plano = ? WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_plano);
            stmt.setInt(2, id_cliente);
            stmt.executeUpdate();

            System.out.println("Vínculo cadastrado com sucesso!");
        }
    }

    // helper de data
    private java.sql.Date parseDate(String dateString) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        java.util.Date parsedUtilDate = sdf.parse(dateString);

        return new java.sql.Date(parsedUtilDate.getTime());
    }
}