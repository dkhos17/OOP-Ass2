package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
	Piece S, SQ, ST, L, pc[];

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		S = new Piece(Piece.S1_STR);
		L = new Piece(Piece.L1_STR);
		SQ = new Piece(Piece.SQUARE_STR);
		ST = new Piece(Piece.STICK_STR);
		pc = Piece.getPieces();
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample0() {
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		assertTrue(b.getGrid(0, 0));
		assertTrue(b.getGrid(1, 1));
		assertFalse(b.getGrid(1, 2));
		assertFalse(b.getGrid(2, 2));
		b.sanityCheck();
		Board t = new Board(4, 4);
		assertFalse(t.getGrid(0, 0));
		assertTrue(t.getGrid(0, -1));
		assertTrue(t.getGrid(-1, 0));
		assertTrue(t.getGrid(7, 2));
		assertTrue(t.getGrid(2, 9));
		assertTrue(b.getGrid(-1, -2));
		assertEquals(4, t.getWidth());
		assertEquals(4, t.getHeight());
		assertEquals(1, t.place(pc[0].fastRotation(), 0, 0));
		t.commit();
		assertEquals(1, t.place(pc[0].fastRotation(), 0, 1));
		t.commit();
		assertEquals(1, t.place(pc[0].fastRotation(), 0, 2));
		t.commit();
//		System.out.println(t);
		assertEquals('|', t.toString().charAt(0));
		assertEquals(' ', t.toString().charAt(1));
		assertEquals(3, t.clearRows());
		t.commit();
		assertEquals(2, t.place(pc[0].fastRotation(), 2, 2));
		t.undo(); t.commit(); t.sanityCheck();
		assertEquals(1, t.place(pc[0].fastRotation(), 0, 0));
		t.commit();
		assertEquals(3, t.place(pc[0].fastRotation(), 0, 0));
		
	}

	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		b.undo();
		b.commit();
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(0, b.place(pc[0], 0, 0));
		b.sanityCheck();
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		b.sanityCheck();
		b.undo();
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.dropHeight(pyr2, 1));
		assertEquals(1, b.dropHeight(pyr4, 0));
	}
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	@Test
	public void testSample3() {
		b.undo();
		b.commit();
		assertEquals(0, b.place(S, 0, 0));
		b.commit();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(2, b.getMaxHeight());
		assertEquals(1, b.place(pc[6].computeNextRotation().computeNextRotation().computeNextRotation(), 0, 1));
		b.commit(); b.sanityCheck();
		assertEquals(4, b.getMaxHeight());
		assertEquals(1, b.clearRows());
		b.commit();
		assertEquals(3, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(1, b.getRowWidth(2));
		assertEquals(1, b.place(pc[0], 2, 0));
		b.commit();
		assertEquals(2, b.clearRows());
		b.commit();
		assertEquals(2, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.place(pc[3].fastRotation(), 0, 0));
		assertEquals(3, b.getMaxHeight());
		assertEquals(2, b.clearRows());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		b.undo();
		b.undo();
		b.undo();
		b.sanityCheck();
		assertEquals(2, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(1, b.place(pc[3].fastRotation(), 0, 0));
		b.commit();
		assertEquals(3, b.getMaxHeight());
		assertEquals(2, b.clearRows());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		b.undo();
		b.undo();
		assertEquals(3, b.getMaxHeight());
		assertEquals(2, b.clearRows());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		
	}
	
	@Test
	public void testSample4() {
		b.undo();
		b.commit();
		assertEquals(0, b.dropHeight(SQ, 0));
		assertEquals(0, b.dropHeight(SQ, 1));
		assertEquals(0, b.place(SQ, 0, 0));
		b.commit();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(2, b.getMaxHeight());
		assertEquals(1, b.place(pc[0], 2, 0));
		b.commit();
		assertEquals(2, b.clearRows());
		b.commit();
		b.sanityCheck();
		assertEquals(2, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(0, b.dropHeight(SQ, 0));
		assertEquals(0, b.dropHeight(pyr2, 0));
		assertEquals(1, b.place(SQ, 0, 0));
		b.commit();
		assertEquals(2, b.clearRows());
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		b.undo();
		b.undo();
		assertEquals(2, b.clearRows());
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.dropHeight(ST, 0));
		assertEquals(0, b.dropHeight(ST, 1));
		assertEquals(0, b.dropHeight(ST, 2));
	}
	
	@Test
	public void testSample5() {
		b.undo(); b.commit();
		assertEquals(0, b.place(L, 0, 0));
		b.commit();
		assertEquals(0, b.clearRows());
		b.commit();
		assertEquals(3, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(1, b.place(new Piece("1 0  1 1  1 2  0 2"), 1, 0));
		b.commit();
		assertEquals(3, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(2, b.clearRows());
		assertEquals(1, b.getMaxHeight());
		assertEquals(2, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));
		b.undo();
		assertEquals(3, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.clearRows());
		b.commit();
		assertEquals(1, b.place(pyr3, 0, 0));
		b.commit();
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(3, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(2, b.clearRows());
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		b.undo();
		assertEquals(2, b.dropHeight(pyr1, 0));
		assertEquals(1, b.place(pyr1, 0, 2));
		b.commit();
		assertEquals(3, b.getRowWidth(0));
		assertEquals(3, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(3, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(3, b.clearRows());
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(4));
		b.sanityCheck();
	}
	
	@Test
	public void testSample6() {
		b.commit();
		assertEquals(1, b.clearRows());
		b.commit();
		assertEquals(1, b.place(pyr3, 0, 1));
		b.commit();
		assertEquals(1, b.clearRows());
		b.commit();
		assertEquals(1, b.place(pyr3, 0, 2));
		b.commit();
		assertEquals(1, b.clearRows());
		b.commit();
		assertEquals(0, b.place(pc[0], 0, 0));
		b.commit();
		assertEquals(1, b.place(pc[0], 2, 0));
		b.commit();
		assertEquals(3, b.getRowWidth(0));
		assertEquals(3, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(2, b.getRowWidth(3));
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(4, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(3, b.clearRows());
		b.undo();
		assertEquals(3, b.getRowWidth(2));
		assertEquals(2, b.getRowWidth(3));
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		b.undo();
		assertEquals(3, b.getRowWidth(2));
		assertEquals(2, b.getRowWidth(3));
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(2));
		assertEquals(3, b.clearRows());
		b.commit();
		assertEquals(0, b.dropHeight(pyr3, 0));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.place(pyr3, 0, 0));
		b.commit();
		assertEquals(3, b.getRowWidth(0));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getMaxHeight());
		assertEquals(2, b.clearRows());
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getMaxHeight());
		b.commit();
		assertEquals(0, b.place(SQ, 0, 0));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getRowWidth(1));
		b.undo(); b.sanityCheck();
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.place(SQ, 0, 0));
		b.commit();
		assertEquals(2, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
	}
	
}
