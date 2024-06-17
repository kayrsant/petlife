CREATE TABLE usuarios(
                         id SERIAL PRIMARY KEY,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         user_role VARCHAR(50) NOT NULL,
                         criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         atualizado_em TIMESTAMP WITH TIME ZONE
);

CREATE TABLE clientes(
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         telefone VARCHAR(11) NOT NULL,
                         cep VARCHAR(9) NOT NULL,
                         cpf VARCHAR(11)
);

CREATE TABLE pets(
                     id SERIAL PRIMARY KEY,
                     nome VARCHAR(100) NOT NULL,
                     idade INTEGER NOT NULL,
                     raca VARCHAR(100) NOT NULL,
                     tipo VARCHAR(100) NOT NULL,
                     peso VARCHAR(100),
                     genero VARCHAR(100) NOT NULL,
                     alergias VARCHAR(100),
                     em_tratamento VARCHAR(100),
                     id_cliente INT NOT NULL,
                     FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

CREATE TABLE funcionarios (
                              id SERIAL PRIMARY KEY,
                              cargo VARCHAR(100) NOT NULL,
                              nome VARCHAR(100) NOT NULL,
                              email VARCHAR(100) NOT NULL UNIQUE,
                              telefone VARCHAR(11) NOT NULL
);

CREATE TABLE agendamentos (
                              id SERIAL PRIMARY KEY,
                              id_cliente INT NOT NULL REFERENCES clientes(id),
                              id_pet INT NOT NULL REFERENCES pets(id),
                              id_funcionario INT NOT NULL REFERENCES funcionarios(id),
                              data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              valor DECIMAL(10, 2) NOT NULL DEFAULT 0,
                              status VARCHAR(10) NOT NULL DEFAULT 'aberto'
);

CREATE TABLE servicos (
                          id SERIAL PRIMARY KEY,
                          descricao VARCHAR(255) NOT NULL,
                          valor DECIMAL(10, 2) NOT NULL
);

CREATE TABLE produtos (
                          id SERIAL PRIMARY KEY,
                          descricao VARCHAR(255) NOT NULL,
                          valor DECIMAL(10, 2) NOT NULL
);

CREATE TABLE servicos_produtos (
                                   id_servico INT NOT NULL REFERENCES servicos(id),
                                   id_produto INT NOT NULL REFERENCES produtos(id),
                                   quantidade INT NOT NULL DEFAULT 1,
                                   PRIMARY KEY (id_servico, id_produto)
);

CREATE TABLE agendamentos_servicos (
                                       id_agendamento INT NOT NULL REFERENCES agendamentos(id),
                                       id_servico INT NOT NULL REFERENCES servicos(id),
                                       PRIMARY KEY (id_agendamento, id_servico)
);
