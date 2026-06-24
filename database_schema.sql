-- ============================================
-- SCRIPT SQL - Projeto Inadimplência The Best
-- ============================================
-- Este projeto utiliza as tabelas padrão do Sankhya ERP
-- As tabelas principais consultadas são:

-- 1. TGFFIN - Tabela de Financeiro (Títulos a Receber/Pagar)
-- 2. TGFPAR - Tabela de Parceiros (Clientes/Fornecedores)
-- 3. MSDFilaMensagem - Tabela de Fila de Mensagens (E-mails)

-- ============================================
-- CONSULTA PRINCIPAL UTILIZADA NO PROJETO
-- ============================================
-- Esta consulta busca títulos vencidos do cliente "The Best" (codparc = 3930)

SELECT 
    MAT.nomeparc AS Matriz,
    PAR.nomeparc AS NomeParceiro,
    FIN.NUFIN AS CodigoTitulo,
    FIN.dtvenc AS DataVencimento,
    FIN.numnota AS NumeroNota,
    TRUNC(SYSDATE) - TRUNC(FIN.dtvenc) AS DiasVencido,
    FIN.VLRDESDOB AS Valor
FROM TGFFIN FIN
INNER JOIN TGFPAR PAR ON PAR.codparc = FIN.codparc
INNER JOIN TGFPAR MAT ON PAR.codparcmatriz = MAT.codparc
WHERE FIN.RECDESP = 1                          -- Tipo: Receber
  AND FIN.PROVISAO = 'N'                       -- Não é provisão
  AND PAR.CLIENTE = 'S'                        -- É cliente
  AND FIN.DTVENC < TRUNC(CURRENT_DATE)         -- Vencido
  AND FIN.DHBAIXA IS NULL                      -- Não foi baixado (pago)
  AND MAT.codparc = 3930;                      -- Código do parceiro "The Best"

-- ============================================
-- ESTRUTURA DAS TABELAS PRINCIPAIS
-- ============================================

-- TGFFIN - Financeiro
-- Campos utilizados:
--   - NUFIN: Número único do financeiro (PK)
--   - codparc: Código do parceiro
--   - dtvenc: Data de vencimento
--   - numnota: Número da nota
--   - VLRDESDOB: Valor desdobrado
--   - RECDESP: 1=Receber, -1=Pagar
--   - PROVISAO: S/N
--   - DHBAIXA: Data/hora da baixa (pagamento)

-- TGFPAR - Parceiros
-- Campos utilizados:
--   - codparc: Código do parceiro (PK)
--   - nomeparc: Nome do parceiro
--   - codparcmatriz: Código da matriz
--   - CLIENTE: S/N

-- MSDFilaMensagem - Fila de E-mails
-- Campos utilizados:
--   - STATUS: Status do envio
--   - CODCON: Código do contato
--   - TENTENVIO: Tentativa de envio
--   - MENSAGEM: Corpo do e-mail (HTML)
--   - TIPOENVIO: Tipo (E=Email)
--   - MAXTENTENVIO: Máximo de tentativas
--   - ASSUNTO: Assunto do e-mail
--   - EMAIL: Destinatários
--   - CODUSU: Código do usuário
--   - REENVIAR: S/N

-- ============================================
-- OBSERVAÇÕES IMPORTANTES
-- ============================================
-- 1. Este projeto NÃO cria tabelas próprias
-- 2. Ele utiliza as tabelas padrão do Sankhya ERP
-- 3. É necessário ter o Sankhya instalado e configurado
-- 4. O banco de dados é Oracle
-- 5. O código do parceiro "The Best" é 3930 (hardcoded)

-- ============================================
-- CONFIGURAÇÃO NECESSÁRIA NO SANKHYA
-- ============================================
-- Para este projeto funcionar, você precisa:
-- 1. Ter o Sankhya ERP instalado
-- 2. Configurar uma Ação Agendada (ScheduledAction)
-- 3. Apontar para a classe: br.inadThebest.AcAgNotiInadTheBest
-- 4. Definir a periodicidade de execução
-- 5. Garantir que o parceiro com código 3930 existe

-- ============================================
-- EXEMPLO DE DADOS DE TESTE (OPCIONAL)
-- ============================================
-- Se você quiser testar em um ambiente de desenvolvimento,
-- precisaria inserir dados nas tabelas do Sankhya.
-- Isso normalmente é feito pela interface do ERP, não por SQL direto.
