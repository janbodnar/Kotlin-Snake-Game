package com.zetcode

import java.awt.EventQueue
import javax.swing.JFrame

class Snake : JFrame() {

    init {

        initUI()
    }

    private fun initUI() {

        add(Board())

        title = "Snake"
        isResizable = false

        pack()

        setLocationRelativeTo(null)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            EventQueue.invokeLater {
                val ex = Snake()
                ex.isVisible = true
            }
        }
    }
}
