package com.innext.szqb.ui.my.activity

import android.graphics.Canvas
import android.widget.Toast
import com.innext.szqb.R
import com.innext.szqb.base.BaseActivity
import com.innext.szqb.ui.my.contract.PdfContract
import com.innext.szqb.ui.my.presenter.PdfPresenter
import com.lidong.pdf.PDFView
import com.lidong.pdf.listener.OnDrawListener
import com.lidong.pdf.listener.OnLoadCompleteListener
import com.lidong.pdf.listener.OnPageChangeListener


/**
 * Copyright (C), 2011-2017
 * FileName:
 * Author: wangzuzhen
 * Email: wzz@iyingke.cn
 * Date: 2017/9/27 ${Time}
 * Description:查看pdf文件
 * History:
 * <Author>      <Time>    <version>    <desc>
 * wangzuzhen
 */
class PdfActivity : BaseActivity<PdfPresenter>(), PdfContract.View, OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {
    private lateinit var pdfView: PDFView
    private lateinit var presenter: PdfPresenter
    private var mUrl: String = ""

    override fun getLayoutId(): Int = R.layout.activity_pdf

    override fun initPresenter() {
        presenter = PdfPresenter()
        presenter.init(this)
    }

    override fun loadData() {
        pdfView = findViewById(R.id.pdfView) as PDFView
        mUrl = intent.getStringExtra("url")
        displayFromFile(mUrl, "hkuaidai.pdf")
    }

    private fun displayFromFile(fileUrl: String, fileName: String) {
        pdfView.fileFromLocalStorage(this, this, this, fileUrl, fileName)   //设置pdf文件地址
    }


    override fun showLoading(content: String?) {
    }

    override fun stopLoading() {
    }

    override fun showErrorMsg(msg: String?, type: String?) {
    }


    /**
     * 翻页回调
     * @param page
     * @param pageCount
     */
    override fun onPageChanged(page: Int, pageCount: Int) {
        Toast.makeText(this, "当前页：" + page +
                "，总页数：" + pageCount, Toast.LENGTH_SHORT).show()
    }

    /**
     * 加载完成回调
     * @param nbPages  总共的页数
     */
    override fun loadComplete(nbPages: Int) {
        Toast.makeText(this, "加载完成" + nbPages, Toast.LENGTH_SHORT).show()
    }

    override fun onLayerDrawn(canvas: Canvas?, pageWidth: Float, pageHeight: Float, displayedPage: Int) {
    }


}