
import `in`.ajm.sb_library.R
import android.content.Context
import android.os.Environment
import android.support.annotation.StringRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.*
import br.tiagohm.breadcrumbview.BreadCrumbItem
import br.tiagohm.breadcrumbview.BreadCrumbView
import br.tiagohm.easyadapter.EasyAdapter
import com.afollestad.materialdialogs.MaterialDialog
import java.io.File
import java.io.FileFilter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class MaterialFileChooser(val context: Context,
                               val allowMultipleFiles: Boolean = false,
                               val allowCreateFolder: Boolean = false,
                               var initialFolder: File = Environment.getExternalStorageDirectory(),
                               val allowSelectFolder: Boolean = false,
                               val minSelectedFiles: Int = 0,
                               val maxSelectedFiles: Int = Int.MAX_VALUE,
                               val showHiddenFiles: Boolean = false,
                               val showFoldersFirst: Boolean = true,
                               val showFiles: Boolean = true,
                               val showFolders: Boolean = true,
                               val allowBrowsing: Boolean = true,
                               val restoreFolder: Boolean = true) {
    
    //Constantes.
    private val typedValue = TypedValue()
    private val theme = context.theme
    private val listaDeArquivosEPastasAdapter = EasyAdapter()
    private val pilhaDeCaminhos = LinkedList<File>()
    private val selecionarTudoStatus = ConcurrentHashMap<File, Boolean>()
    private val filters = ArrayList<Filter>()
    private val chooserTextWatcher = ChooserTextWatcher()
    private val chooserFileFilter = ChooserFileFilter()
    private val corDeFundo: Int by lazy {
        theme.resolveAttribute(R.attr.mfc_theme_background, typedValue, true)
        typedValue.data
    }
    private val corDeFrente: Int by lazy {
        theme.resolveAttribute(R.attr.mfc_theme_foreground, typedValue, true)
        typedValue.data
    }
    private val corDoTitulo: Int by lazy {
        theme.resolveAttribute(R.attr.mfc_theme_title, typedValue, true)
        typedValue.data
    }
    private val corDoBotaoCancelar: Int by lazy {
        theme.resolveAttribute(R.attr.mfc_theme_cancel_button, typedValue, true)
        typedValue.data
    }
    private val corDoBotaoOK: Int by lazy {
        theme.resolveAttribute(R.attr.mfc_theme_ok_button, typedValue, true)
        typedValue.data
    }
    
    //Variáveis.
    private var titulo: CharSequence? = ""
    private val arquivosSelecionados: MutableSet<File> = Collections.newSetFromMap(ConcurrentHashMap<File, Boolean>())
    private var arquivoAnteriormenteSelecionadoCb: CheckBox? = null
    private var arquivoAnteriormenteSelecionado: File? = null
    private lateinit var pastaAtual: File
    private var ordenacao: Sorter = Sorter.ByNameInAscendingOrder
    private lateinit var janela: DialogBuilder
    private var textDaBusca = ""
    private var arquivosAtuais: List<File> = Collections.emptyList()
    private var tamanhoTotalDosArquivosSelecionados = 0L
    private var onSelectedFilesListener: (files: List<File>) -> Unit = {}
    
    init {
        //Pasta inicial padrão.
        definirPastaInicial(ChooserSharedPreference.getPreviouslySelectedDiretory(context, restoreFolder, initialFolder))
    }
    
    /**
     * Define o título da janela.
     */
    fun title(title: CharSequence?): MaterialFileChooser {
        titulo = title
        return this
    }
    
    /**
     * Define o título da janela.
     */
    fun title(@StringRes resId: Int): MaterialFileChooser {
        return title(context.getText(resId))
    }
    
    /**
     * Define o método de ordenação da listagem de arquivos e pastas.
     */
    fun sorter(sorter: Sorter): MaterialFileChooser {
        ordenacao = sorter
        return this
    }
    
    fun onSelectedFilesListener(listener: (files: List<File>) -> Unit): MaterialFileChooser {
        onSelectedFilesListener = listener
        return this
    }
    
    private fun definirPastaInicial(folder: File): MaterialFileChooser {
        initialFolder = folder
        pastaAtual = folder
        //Limpa a pilha
        pilhaDeCaminhos.clear()
        //Insere na pilha.
        pilhaDeCaminhos.addFirst(pastaAtual)
        //Estado desta pasta.
        selecionarTudoStatus[pastaAtual] = false
        return this
    }
    
    private fun exibirBreadCrumbView(file: File) {
        var mfile = file
        //Limpa.
        janela.mCaminhoDoDiretorio.itens.clear()
        //Verifica se não é uma pasta.
        if (!mfile.isFolder) {
            mfile = mfile.parentFile
        }
        //Obtém a pasta-pai.
        val parent = mfile.parentFile
        //Não tem pasta-pai.
        if (parent == null) {
            val item = RootFileBreadCrumbItem(mfile)
            janela.mCaminhoDoDiretorio.addItem(item)
        }
        //Tem pasta-pai.
        else {
            exibirBreadCrumbView(parent)
            val item = FileBreadCrumbItem(mfile)
            janela.mCaminhoDoDiretorio.addItem(item)
        }
    }
    
    private fun selecionarArquivo(buttonView: CompoundButton?, file: File, selecionar: Boolean) {
        //Checkbox selecionado.
        if (selecionar) {
            //Não é multi-selecionável e tem um arquivo selecionado.
            if (!allowMultipleFiles && arquivoAnteriormenteSelecionadoCb != null) {
                val cb = arquivoAnteriormenteSelecionadoCb!!
                arquivoAnteriormenteSelecionadoCb = null
                //Dois arquivos que estão na mesma pasta.
                if (buttonView !== cb && file.parent == arquivoAnteriormenteSelecionado?.parent) {
                    //Desmarca o que está selecionado.
                    cb.isChecked = false
                } else {
                    //Remove o que está selecionado.
                    arquivosSelecionados.remove(arquivoAnteriormenteSelecionado)
                    arquivoAnteriormenteSelecionado = null
                }
            }
            //Adiciona o arquivo.
            if (!arquivosSelecionados.contains(file)) {
                tamanhoTotalDosArquivosSelecionados += if (file.isFolder) 0 else file.length()
            }
            arquivosSelecionados.add(file)
        } else {
            //Remove o arquivo.
            if (arquivosSelecionados.contains(file)) {
                tamanhoTotalDosArquivosSelecionados -= if (file.isFolder) 0 else file.length()
            }
            arquivosSelecionados.remove(file)
            arquivoAnteriormenteSelecionado = null
        }
        //Marca o arquivo que foi selecionado.
        arquivoAnteriormenteSelecionadoCb = buttonView as CheckBox?
        arquivoAnteriormenteSelecionado = file
        //Atualiza o número de pastas selecionadas de acordo com a pluralidade.
        janela.exibirQuantidadeDeItensSelecionados(tamanhoTotalDosArquivosSelecionados)
    }
    
    /**
     * Vá para uma pasta específica.
     */
    fun goTo(file: File) {
        //Não permitir a navegação.
        if (!allowBrowsing) {
            //nada
        }
        //Navegar somente se for uma pasta.
        else if (file.isFolder) {
            pastaAtual = file
            pilhaDeCaminhos.addFirst(pastaAtual)
            //Ainda não navegou nesta pasta. O selecionar tudo está desabilitado.
            if (allowMultipleFiles && !selecionarTudoStatus.containsKey(file)) {
                selecionarTudoStatus[file] = false
            }
            carregarPastaAtual()
            //Define o estado do botão selecionar tudo.
            janela.mSelecionarTudo.isChecked = allowMultipleFiles && selecionarTudoStatus[file]!!
        }
    }
    
    /**
     * vá para a pasta inicial.
     */
    fun goToStart() {
        goTo(initialFolder)
    }
    
    private fun backTo(file: File): Boolean {
        //Pode navegar e é uma pasta.
        return if (allowBrowsing && file.isFolder) {
            pastaAtual = file
            carregarPastaAtual()
            //Define o estado do botão selecionar tudo.
            janela.mSelecionarTudo.isChecked = allowMultipleFiles && selecionarTudoStatus[file] ?: false
            true
        } else {
            false
        }
    }
    
    /**
     * Vá para a pasta anterior.
     */
    fun back(): Boolean {
        //Se pode navegar e há pasta pra navegar.
        return if (allowBrowsing && pilhaDeCaminhos.size > 1) {
            //Remove a pasta atual da pilha.
            pilhaDeCaminhos.removeFirst()
            //Retorna para a pasta anterior.
            backTo(pilhaDeCaminhos.first)
        } else {
            false
        }
    }
    
    private fun compareFile(a: File, b: File): Int {
        //Ordena por pastas primeiro.
        return if (showFoldersFirst) {
            when {
                a.isDirectory == b.isDirectory -> ordenacao.compare(a, b)
                a.isDirectory -> -1
                else -> 1
            }
        } else {
            when {
                a.isFile == b.isFile -> ordenacao.compare(a, b)
                a.isFile -> -1
                else -> 1
            }
        }
    }
    
    private fun scanFiles(file: File): List<File> {
        //Obtém a lista de arquivos.
        val fileList = file.listFiles(chooserFileFilter)?.toMutableList() ?: Collections.emptyList()
        //Seta a quantidade de itens exibidos.
        janela.mQuantidadeDeItens.text = fileList.size.toString()
        //Orderná-la.
        fileList.sortWith(Comparator { a, b -> compareFile(a, b) })
        return fileList
    }
    
    private fun exibirRecyclerView(file: File) {
        arquivosAtuais = scanFiles(file)
        listaDeArquivosEPastasAdapter.setData(arquivosAtuais)
    }
    
    private fun carregarPastaAtual() {
        exibirBreadCrumbView(pastaAtual)
        exibirRecyclerView(pastaAtual)
        janela.mTamanhoTotal.text = pastaAtual.sizeAsString
    }
    
    /**
     * Exibe a janela.
     */
    fun show() {
        DialogBuilder()
        //Exibe a janela de diálogo.
        janela.show()
    }
    
    private inner class DialogBuilder : MaterialDialog.Builder(context) {
        
        //Views da janela de dialogo.
        val mTitulo: TextView by lazy { customView.findViewById<TextView>(R.id.titulo) }
        val mCaminhoDoDiretorio: BreadCrumbView<File> by lazy { customView.findViewById<BreadCrumbView<File>>(R.id.caminhoDoDiretorio) }
        val mListaDeArquivosEPastas: RecyclerView by lazy { customView.findViewById<RecyclerView>(R.id.listaDeArquivosEPastas) }
        val mTamanhoTotal: TextView by lazy { customView.findViewById<TextView>(R.id.tamanhoTotal) }
        val mQuantidadeDeItens: TextView by lazy { customView.findViewById<TextView>(R.id.quantidadeDeItens) }
        val mBotaoVoltar: ImageView by lazy { customView.findViewById<ImageView>(R.id.botaoVoltar) }
        val mIrParaDiretorioInicial: ImageView by lazy { customView.findViewById<ImageView>(R.id.irParaDiretorioInicial) }
        val mQuantidadeDeItensSelecionados: TextView by lazy { customView.findViewById<TextView>(R.id.quantidadeDeItensSelecionados) }
        val mBotaoBuscar: ImageView by lazy { customView.findViewById<ImageView>(R.id.botaoBuscar) }
        val mCampoDeBusca: EditText by lazy { customView.findViewById<EditText>(R.id.campoDeBusca) }
        val mCampoDeBuscaBox: View by lazy { customView.findViewById<View>(R.id.campoDeBuscaBox) }
        val mSwipeRefreshLayout: SwipeRefreshLayout by lazy { customView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout) }
        val mSelecionarTudo: CheckBox by lazy { customView.findViewById<CheckBox>(R.id.botaoSelecionarTudo) }
        val mBotaoCriarPasta: FloatingActionButton by lazy { customView.findViewById<FloatingActionButton>(R.id.criarPasta) }
        
        init {
            janela = this
            //Seta o layout.
            customView(R.layout.dialog_file_chooser, false)
            //Seta o título.
            if (titulo.isNullOrEmpty()) mTitulo.visibility = View.GONE else mTitulo.text = titulo
            //Botões.
            positiveText(android.R.string.ok)
            negativeText(android.R.string.cancel)
            //Tema.
            backgroundColor(corDeFundo)
            positiveColor(corDeFrente)
            negativeColor(corDeFrente)
            mSwipeRefreshLayout.setColorSchemeColors(corDeFrente)
            //Comportamento.
            cancelable(false)
            canceledOnTouchOutside(false)
            autoDismiss(false)
            //Configura o RecyclerView.
            mListaDeArquivosEPastas.layoutManager = LinearLayoutManager(context)
            listaDeArquivosEPastasAdapter.map<File>(R.layout.file_item) { file, injector ->
                //Seta o ícone do arquivo.
                injector.image(R.id.iconeDoArquivo,
                        if (file.isFolder) R.drawable.pasta else getIconByExtension(file))
                //Seta o ícone de arquivo protegido.
                injector.image(R.id.protecaoDoArquivo,
                        if (file.isProtected) R.drawable.cadeado else 0)
                //Seta o ícone de que contém arquivos selecionados.
                injector.image(R.id.pastaComItensSelecionados,
                        if (file.isFolder && constainsSelectedFiles(file)) R.drawable.asterisco else 0)
                //Seta a opacidade se o arquivo é oculto.
                injector.using<View>(R.id.iconeDoArquivo) { alpha = if (file.isHidden) 0.4f else 1f }
                //Seta o texto com o nome do arquivo.
                injector.text(R.id.nomeDoArquivo, file.name)
                //Seta o tamanho do arquivo.
                if (file.isFile) {
                    //Tamanho em bytes.
                    injector.text(R.id.tamanhoDoArquivo, file.sizeAsString)
                }
                //Tamanho em quantidade de itens.
                else {
                    //Obtém a quantidade de itens.
                    val itemCount = file.count(chooserFileFilter)
                    //Formatação do texto de acordo com a pluralidade.
                    val stringRes = if (itemCount > 1) R.string.quantidade_itens_pasta_plural else R.string.quantidade_itens_pasta_singular
                    injector.text(R.id.tamanhoDoArquivo, context.getString(stringRes, itemCount))
                }
                //Seta a data de modificação do arquivo.
                injector.text(R.id.dataDaUltimaModificacao, file.lastModified)
                //Permitir selecionar pastas.
                injector.show(R.id.botaoSelecionarArquivo, allowSelectFolder || file.isFile)
                //Eventos.
                injector.click<View>(EasyAdapter.ROOT_VIEW) {
                    //Vá para a pasta clicada.
                    goTo(file)
                }
                //Selecionei/Deselecionei um arquivo.
                injector.using<CheckBox>(R.id.botaoSelecionarArquivo) {
                    tag = file
                    //Marcar checkbox se este é de um arquivo selecionado.
                    setOnCheckedChangeListener(null)
                    isChecked = arquivosSelecionados.contains(file)
                    //Evento.
                    setOnCheckedChangeListener({ buttonView, isChecked ->
                        //Seleciona ou deseleciona o arquivo.
                        selecionarArquivo(buttonView, file, isChecked)
                    })
                }
            }
            listaDeArquivosEPastasAdapter.mapEmpty(R.layout.dialog_empty_folder)
            //Atualizar.
            mSwipeRefreshLayout.setOnRefreshListener {
                carregarPastaAtual()
                mSwipeRefreshLayout.isRefreshing = false
            }
            //Selecionar tudo.
            mSelecionarTudo.setOnCheckedChangeListener { _, checked ->
                //Seleciona ou não os arquivo da pasta atual.
                selecionarTudoStatus[pastaAtual] = checked
                arquivosAtuais.forEach {
                    if (allowSelectFolder || !it.isFolder) {
                        selecionarArquivo(null, it, checked)
                    }
                }
                //Recarrega os itens exibidos.
                carregarPastaAtual()
            }
            //Migalha.
            mCaminhoDoDiretorio.setBreadCrumbListener(object : BreadCrumbView.BreadCrumbListener<File> {
                override fun onItemClicked(breadCrumbView: BreadCrumbView<File>, breadCrumbItem: BreadCrumbItem<File>, i: Int) {
                    goTo(breadCrumbItem.selectedItem)
                }
                
                override fun onItemValueChanged(breadCrumbView: BreadCrumbView<File>, breadCrumbItem: BreadCrumbItem<File>, i: Int, file: File, t1: File): Boolean {
                    return false
                }
            })
            //Voltar para a pasta anterior.
            mBotaoVoltar.setOnClickListener { back() }
            //Voltar para o inicio.
            mIrParaDiretorioInicial.setOnClickListener { goToStart() }
            //Pesquisar.
            mBotaoBuscar.setOnClickListener {
                if (mCampoDeBuscaBox.visibility == View.VISIBLE) {
                    mCampoDeBuscaBox.visibility = View.GONE
                } else {
                    mCampoDeBuscaBox.visibility = View.VISIBLE
                }
            }
            //Evento de pesquisa.
            mCampoDeBusca.removeTextChangedListener(chooserTextWatcher)
            mCampoDeBusca.addTextChangedListener(chooserTextWatcher)
            listaDeArquivosEPastasAdapter.attachTo(mListaDeArquivosEPastas)
            //Criar pasta.
            mBotaoCriarPasta.setOnClickListener {
                MaterialDialog.Builder(context)
                        .title(R.string.criar_pasta_title)
                        .titleColor(corDoTitulo)
                        .inputRangeRes(1, -1, R.color.criar_pasta_input_out_range)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .negativeText(android.R.string.cancel)
                        .negativeColor(corDoBotaoCancelar)
                        .positiveColor(corDoBotaoOK)
                        .backgroundColor(corDeFundo)
                        .input(R.string.criar_pasta_edittext_hint, 0, false, { _, input ->
                            val novaPasta = File(pastaAtual, input.toString())
                            try {
                                if (!novaPasta.mkdir()) {
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                                } else {
                                    carregarPastaAtual()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "error: " + e.message, Toast.LENGTH_SHORT).show()
                            }
                        }).show()
            }
            //Eventos.
            onPositive { dialog, _ ->
                //Selecionou a quantidade mínima de arquivos.
                if (arquivosSelecionados.size in minSelectedFiles..maxSelectedFiles) {
                    onSelectedFilesListener(arquivosSelecionados.toList())
                    //Fecha a janela.
                    dialog.dismiss()
                }
                
            }
            onNegative { dialog, _ ->
                //Selecionou a quantidade mínima de arquivos.
                if (arquivosSelecionados.size in minSelectedFiles..maxSelectedFiles) {
                    //Fecha a janela.
                    dialog.dismiss()
                }
            }
            dismissListener {
                //Salva a pasta atual se está permitido restaurar pastas.
                if (restoreFolder) {
                    ChooserSharedPreference.setPreviouslySelectedDiretory(context, pastaAtual)
                }
            }
            //Permite seleção multipla.
            mSelecionarTudo.visibility = if (allowMultipleFiles) View.VISIBLE else View.GONE
            //Permitir criar pasta.
            janela.mBotaoCriarPasta.visibility = if (allowCreateFolder) View.VISIBLE else View.GONE
            //Inicio.
            exibirQuantidadeDeItensSelecionados(0)
            carregarPastaAtual()
        }
        
        fun exibirQuantidadeDeItensSelecionados(tamanho: Long) {
            //Atualiza o número de pastas selecionadas de acordo com a pluralidade.
            if (arquivosSelecionados.size > 1) {
                janela.mQuantidadeDeItensSelecionados.text =
                        context.getString(R.string.quantidade_itens_selecionados_plural, arquivosSelecionados.size, tamanho.toSizeString())
            } else {
                janela.mQuantidadeDeItensSelecionados.text =
                        context.getString(R.string.quantidade_itens_selecionados_singular, arquivosSelecionados.size, tamanho.toSizeString())
            }
        }
        
        private fun constainsSelectedFiles(parent: File): Boolean {
            for (file in arquivosSelecionados) {
                if (file.absolutePath != parent.absolutePath &&
                        file.absolutePath.startsWith(parent.absolutePath)) {
                    return true
                }
            }
            return false
        }
        
        private fun getIconByExtension(file: File): Int {
            return when (file.extension) {
                "mp4" -> R.drawable.video
                "c", "cpp", "cs", "js", "h", "java", "kt", "php", "xml" -> R.drawable.codigo
                "avi" -> R.drawable.avi
                "doc" -> R.drawable.doc
                "flv" -> R.drawable.flv
                "jpg", "jpeg" -> R.drawable.jpg
                "json" -> R.drawable.json
                "mov" -> R.drawable.mov
                "mp3" -> R.drawable.mp3
                "pdf" -> R.drawable.pdf
                "txt" -> R.drawable.txt
                else -> R.drawable.arquivo
            }
        }
    }
    
    private object ChooserSharedPreference {
        
        private const val NAME = "materialfilechooser_prefs"
        private const val PREVIOUS_SELECTED_FOLDER = "prev_selected_folder"
        
        fun getPreviouslySelectedDiretory(context: Context, restoreFolder: Boolean, initialFolder: File): File {
            if (!restoreFolder) return initialFolder
            val path = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                    .getString(PREVIOUS_SELECTED_FOLDER, null)
            return if (path != null) File(path) else initialFolder
        }
        
        fun setPreviouslySelectedDiretory(context: Context, file: File) {
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(PREVIOUS_SELECTED_FOLDER, file.absolutePath)
                    .apply()
        }
    }
    
    //Item para a pasta raiz.
    private class RootFileBreadCrumbItem(file: File) : FileBreadCrumbItem(file) {
        
        override fun getText() = "/"
    }
    
    //Item para uma pasta.
    private open class FileBreadCrumbItem(file: File) : BreadCrumbItem<File>() {
        
        init {
            itens = listOf(file)
        }
        
        override fun getText(): String? = selectedItem.name
    }
    
    //Filtro de arquivos.
    private inner class ChooserFileFilter : FileFilter {
        
        override fun accept(f: File): Boolean {
            val showHidden = showHiddenFiles || !f.isHidden
            return (textDaBusca.isEmpty() || f.name.contains(textDaBusca, true)) &&
                    //É um arquivo oculto e pode ser exibido.
                    showHidden &&
                    //Exibir arquivos e/ou pastas.
                    (showFiles && f.isFile || showFolders && f.isFolder) &&
                    //Filtros.
                    filter(f)
        }
        
        private fun filter(f: File): Boolean {
            //Não há filtros.
            if (filters.size == 0) return true
            //Filtra.
            for (filter in filters) {
                if (filter.accept(f)) {
                    return true
                }
            }
            return false
        }
    }
    
    private inner class ChooserTextWatcher : TextWatcher {
        
        override fun afterTextChanged(s: Editable) {
            textDaBusca = s.toString().toLowerCase()
            carregarPastaAtual()
        }
        
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }
        
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }
    }
}