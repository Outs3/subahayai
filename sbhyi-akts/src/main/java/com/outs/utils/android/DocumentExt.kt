package com.outs.utils.android

import android.content.Intent
import java.io.File

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/28 18:09
 * desc:
 */
const val MIME_PDF = "application/pdf"
const val MIME_DOC = "application/msword"
const val MIME_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
const val MIME_XLS = "application/vnd.ms-excel"
const val MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
const val MIME_PPT = "application/vnd.ms-powerpoint"
const val MIME_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation"

enum class DocumentMimeType(
    name: String,
    val mimeType: String,
    val alias: Array<String> = arrayOf()
) {
    //Hex: 25 50 44 46 2D 31 2E, ASCII: %PDF-1
    PDF(
        "pdf", MIME_PDF, arrayOf(
            "application/x-pdf",
            "application/acrobat",
            "applications/vnd.pdf",
            "text/pdf",
            "text/x-pdf"
        )
    ),

    //Hex: D0 CF 11 E0 A1 B1 1A E1 00, ASCII: ÐÏ à¡± á
    DOC(
        "doc",
        MIME_DOC,
        arrayOf(
            "application/doc",
            "appl/text",
            "application/vnd.msword",
            "application/vnd.ms-word",
            "application/winword",
            "application/word",
            "application/x-msw6",
            "application/x-msword"
        )
    ),

    //Hex: 50 4B 03 04, ASCII: PK
    DOCX("docx", MIME_DOCX),

    //Hex: D0 CF 11 E0 A1 B1 1A E1 00, ASCII: ÐÏ à¡± á
    XLS(
        "xls",
        MIME_XLS,
        arrayOf(
            "application/xls",
            "application/excel",
            "application/msexcel",
            "application/x-msexcel",
            "application/x-ms-excel",
            "application/vnd.ms-excel",
            "application/x-excel",
            "application/x-dos_ms_excel"
        )
    ),

    //Hex: 50 4B 03 04, ASCII: PK
    XLSX("xlsx", MIME_XLSX),

    //Hex: D0 CF 11 E0 A1 B1 1A E1 00 00 00 00
    PPT(
        "ppt", MIME_PPT, arrayOf(
            "application/mspowerpoint",
            "application/ms-powerpoint",
            "application/mspowerpnt",
            "application/vnd-mspowerpoint",
            "application/powerpoint",
            "application/x-powerpoint",
            "application/x-m"
        )
    ),

    //Hex: 50 4B 03 04, ASCII: PK
    PPTX("pptx", MIME_PPTX);

    fun isExtOf(ext: String): Boolean = name.equals(ext, true)

    fun isExtOf(file: File): Boolean = file.extension.let(::isExtOf)

    fun isMimeTypeOf(value: String): Boolean = mimeType == value || alias.contains(value)
}

fun File.asDocumentMimeType(): String? =
    DocumentMimeType.values().firstOrNull { it.isExtOf(this) }?.mimeType

fun File.isDocumentFile() =
    extension.let { ext -> DocumentMimeType.values().any { it.isExtOf(ext) } }

fun isDocumentFile(mimeType: String) = DocumentMimeType.values().any { it.isMimeTypeOf(mimeType) }

fun createDocument(document: File): Intent {
    if (!document.isDocumentFile()) throw RuntimeException("${document.name}不是一个有效的文档类型文件！")
    val name = document.name
    val mimeType = document.asDocumentMimeType()

    return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = mimeType
        putExtra(Intent.EXTRA_TITLE, name)
    }
}