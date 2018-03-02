package com.zetcode

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.Timer


class Board : JPanel(), ActionListener {

    private val boardWidth = 300
    private val boardHeight = 300
    private val dotSize = 10
    private val allDots = 900
    private val randPos = 29
    private val delay = 140

    private val x = IntArray(allDots)
    private val y = IntArray(allDots)

    private var nOfDots: Int = 0
    private var appleX: Int = 0
    private var appleY: Int = 0

    private var leftDirection = false
    private var rightDirection = true
    private var upDirection = false
    private var downDirection = false
    private var inGame = true

    private var timer: Timer? = null
    private var ball: Image? = null
    private var apple: Image? = null
    private var head: Image? = null

    init {

        addKeyListener(TAdapter())
        background = Color.black
        isFocusable = true

        preferredSize = Dimension(boardWidth, boardHeight)
        loadImages()
        initGame()
    }

    private fun loadImages() {

        val iid = ImageIcon("src/main/resources/dot.png")
        ball = iid.image

        val iia = ImageIcon("src/main/resources/apple.png")
        apple = iia.image

        val iih = ImageIcon("src/main/resources/head.png")
        head = iih.image
    }

    private fun initGame() {

        nOfDots = 3

        for (z in 0 until nOfDots) {
            x[z] = 50 - z * 10
            y[z] = 50
        }

        locateApple()

        timer = Timer(delay, this)
        timer!!.start()
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        doDrawing(g)
    }

    private fun doDrawing(g: Graphics) {

        if (inGame) {

            g.drawImage(apple, appleX, appleY, this)

            for (z in 0 until nOfDots) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this)
                } else {
                    g.drawImage(ball, x[z], y[z], this)
                }
            }

            Toolkit.getDefaultToolkit().sync()

        } else {

            gameOver(g)
        }
    }

    private fun gameOver(g: Graphics) {

        val msg = "Game Over"
        val small = Font("Helvetica", Font.BOLD, 14)
        val fontMetrics = getFontMetrics(small)

        val rh = RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)

        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY

        (g as Graphics2D).setRenderingHints(rh)

        g.color = Color.white
        g.font = small
        g.drawString(msg, (boardWidth - fontMetrics.stringWidth(msg)) / 2, boardHeight / 2)
    }

    private fun checkApple() {

        if (x[0] == appleX && y[0] == appleY) {

            nOfDots++
            locateApple()
        }
    }

    private fun move() {

        for (z in nOfDots downTo 1) {
            x[z] = x[z - 1]
            y[z] = y[z - 1]
        }

        if (leftDirection) {
            x[0] -= dotSize
        }

        if (rightDirection) {
            x[0] += dotSize
        }

        if (upDirection) {
            y[0] -= dotSize
        }

        if (downDirection) {
            y[0] += dotSize
        }
    }

    private fun checkCollision() {

        for (z in nOfDots downTo 1) {

            if (z > 4 && x[0] == x[z] && y[0] == y[z]) {
                inGame = false
            }
        }

        if (y[0] >= boardHeight) {
            inGame = false
        }

        if (y[0] < 0) {
            inGame = false
        }

        if (x[0] >= boardWidth) {
            inGame = false
        }

        if (x[0] < 0) {
            inGame = false
        }

        if (!inGame) {
            timer!!.stop()
        }
    }

    private fun locateApple() {

        var r = (Math.random() * randPos).toInt()
        appleX = r * dotSize

        r = (Math.random() * randPos).toInt()
        appleY = r * dotSize
    }

    override fun actionPerformed(e: ActionEvent) {

        if (inGame) {

            checkApple()
            checkCollision()
            move()
        }

        repaint()
    }

    private inner class TAdapter : KeyAdapter() {

        override fun keyPressed(e: KeyEvent?) {

            val key = e!!.keyCode

            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true
                upDirection = false
                downDirection = false
            }

            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true
                upDirection = false
                downDirection = false
            }

            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true
                rightDirection = false
                leftDirection = false
            }

            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true
                rightDirection = false
                leftDirection = false
            }
        }
    }
}
