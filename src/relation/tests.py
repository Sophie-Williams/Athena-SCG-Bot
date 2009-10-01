import relation
import gen
import unittest

class TestRelation(unittest.TestCase):

  def test_get_magic_number(self):
    self.assertEqual(relation.get_magic_number(4, 3, 1), 65280)

  def test_get_mask(self):
    formula = lambda x: (2 ** (2 ** x)) - 1
    self.assertEqual(relation.get_mask(1), formula(1))
    self.assertEqual(relation.get_mask(2), formula(2))
    self.assertEqual(relation.get_mask(3), formula(3))
    self.assertEqual(relation.get_mask(4), formula(4))
    self.assertEqual(relation.get_mask(5), formula(5))

  def test_is_irrelevant(self):
    # XXX needs more tests
    #   is_irrelevant(rn, rank, var_p)
    # = (reduce(rn, rank, var_p, 0) == reduce(rn, rank, var_p, 1))
    f = relation.is_irrelevant
    g = relation.reduce
    h = lambda x, y, z: g(x, y, z, 0) == g(x, y, z, 1)
    j = lambda x, y, z: f(x, y, z) == h(x, y, z)

    self.assertTrue(j(23423, 3, 3))
    self.assertTrue(j(23423, 3, 2))
    self.assertTrue(j(23423, 3, 1))
    self.assertTrue(j(23423, 3, 0))
    self.assertTrue(j(23421233, 5, 0))
    self.assertTrue(j(23421233, 5, 1))
    self.assertTrue(j(23421233, 5, 2))
    self.assertTrue(j(23421233, 5, 3))
    self.assertTrue(j(23421233, 5, 4))
    self.assertTrue(j(23421233, 5, 5))

  def test_num_relevant_variables(self):
    # XXX needs more tests
    self.assertEqual(relation.num_relevant_variables(123123, 3), 3)

  def test_is_forced(self):
    # XXX needs more tests
    self.assertEqual(relation.is_forced(123123, 3, 1), -1)

  def test_first_forced_variable(self):
    # XXX needs more tests
    self.assertEqual(relation.first_forced_variable(123123, 3, 0), None)

  def test_n_map(self):
    # XXX needs more tests
    self.assertEqual(relation.n_map(123123, 3, 1), 252)

  def test_reduce(self):
    # XXX probably need more tests
    # values extracted from the original Java implementation
    f = relation.reduce
    self.assertEqual(f(150, 3, 2, 0), 102)
    self.assertEqual(f(150, 3, 2, 1), 153)
    self.assertEqual(f(150, 3, 1, 0), 90)
    self.assertEqual(f(150, 3, 1, 1), 165)
    self.assertEqual(f(150, 3, 0, 0), 60)
    self.assertEqual(f(150, 3, 0, 1), 195)
    self.assertEqual(f(231, 3, 2, 0), 119)
    self.assertEqual(f(231, 3, 2, 1), 238)
    self.assertEqual(f(231, 3, 1, 0), 175)
    self.assertEqual(f(231, 3, 1, 1), 245)
    self.assertEqual(f(231, 3, 0, 0), 207)
    self.assertEqual(f(231, 3, 0, 1), 243)

  def test_swap(self):
    # XXX needs more tests
    self.assertEqual(relation.swap(123123, 4, 1, 2), 51407)

  def test_renme(self):
    # XXX what the fuck?
    self.assertEqual(relation.renme(7, 2, relation.SOURCE, [1, 2, 0]), 7)
    self.assertEqual(relation.renme(4, 5, relation.SOURCE, [1, 2, 0]), 4)

  def test_ones(self):
    self.assertEqual(relation.ones(0, 5), 0)
    self.assertEqual(relation.ones(1, 5), 1)
    self.assertEqual(relation.ones(3, 5), 2)
    self.assertEqual(relation.ones(4, 5), 1)
    self.assertEqual(relation.ones(7, 5), 3)
    self.assertEqual(relation.ones(15, 5), 4)
    self.assertEqual(relation.ones(31, 5), 5)
    self.assertEqual(relation.ones(2 ** 32, 5), 0)
    self.assertEqual(relation.ones(2 ** 31, 5), 1)
    self.assertEqual(relation.ones(2 ** 32 - 1, 5), 32)
    self.assertEqual(relation.ones(2 ** 32 - 1 + 2 ** 32, 5), 32)

  def test_q(self):
    # XXX needs more tests
    f = relation.q

    # values extracted from the original Java implementation
    self.assertEqual(f(150, 3, 0), 0)
    self.assertEqual(f(150, 3, 1), 3)
    self.assertEqual(f(150, 3, 2), 0)
    self.assertEqual(f(150, 3, 3), 1)
    self.assertEqual(f(234, 3, 0), 0)
    self.assertEqual(f(234, 3, 1), 1)
    self.assertEqual(f(234, 3, 2), 3)
    self.assertEqual(f(234, 3, 3), 1) 

    self.assertEqual(f(12354312, 5, 4), 2)

  def test_x_true_vars(self):
    f = relation.x_true_vars
    self.assertEqual(f(1, 0), 0b01)
    self.assertEqual(f(1, 1), 0b10)
    self.assertEqual(f(2, 0), 0b0001)
    self.assertEqual(f(2, 1), 0b0110)
    self.assertEqual(f(2, 2), 0b1000)
    self.assertEqual(f(3, 0), 0b00000001)
    self.assertEqual(f(3, 1), 0b00010110)
    self.assertEqual(f(3, 2), 0b01101000)
    self.assertEqual(f(3, 3), 0b10000000)
    self.assertEqual(f(4, 0), 0b0000000000000001)
    self.assertEqual(f(4, 1), 0b0000000100010110)
    self.assertEqual(f(4, 2), 0b0001011001101000)
    self.assertEqual(f(4, 3), 0b0110100010000000)
    self.assertEqual(f(4, 4), 0b1000000000000000)
    self.assertEqual(f(5, 0), 0b00000000000000000000000000000001)
    self.assertEqual(f(5, 1), 0b00000000000000010000000100010110)
    self.assertEqual(f(5, 2), 0b00000001000101100001011001101000)
    self.assertEqual(f(5, 3), 0b00010110011010000110100010000000)
    self.assertEqual(f(5, 4), 0b01101000100000001000000000000000)
    self.assertEqual(f(5, 5), 0b10000000000000000000000000000000)

  def test_break_even(self):
    f = relation.break_even
    for rn in range(1, 255, 2):
      self.assertEqual(f(rn, 3), 1.0)

    self.assertAlmostEqual(f(2, 3), 0.148148148148)
    self.assertAlmostEqual(f(4, 3), 0.148148148148)
    self.assertAlmostEqual(f(6, 3), 0.296296296296)
    self.assertAlmostEqual(f(8, 3), 0.148148148148)
    self.assertAlmostEqual(f(10, 3), 0.25)
    self.assertAlmostEqual(f(12, 3), 0.25)
    self.assertAlmostEqual(f(14, 3), 0.384900179)
    self.assertAlmostEqual(f(16, 3), 0.148148148148)
    self.assertAlmostEqual(f(18, 3), 0.296296296296)
    self.assertAlmostEqual(f(20, 3), 0.296296296296)
    self.assertAlmostEqual(f(22, 3), 0.444444444444)
    # XXX more test cases?

  def test_gen_permute3(self):
    f = gen.permute3
    # for x in f(450):
    #   x

  def test_solve(self):
    clauses = [
      (22, 'v1', 'v2', 'v3'),
      (22, 'v2', 'v3', 'v4'),
      (22, 'v3', 'v4', 'v5'),
    ]
    vars = ('v1', 'v2', 'v3', 'v4', 'v5')
    c_p = relation.Problem(vars, clauses)

    fsat, values = c_p.solve()

    self.assertEquals(fsat, 3)
    self.assertTrue(values in [
        [0, 0, 1, 0, 0],
        [1, 0, 0, 1, 0],
        [1, 0, 0, 0, 1],
        [0, 1, 0, 0, 1],
    ])

    clauses = [
      (8, 'v0', 'v1', 'v2',),
      (8, 'v1', 'v2', 'v3',),
      (8, 'v1', 'v2', 'v3',),
      (8, 'v2', 'v3', 'v4',),
      (8, 'v2', 'v3', 'v4',),
      (8, 'v1', 'v2', 'v3',),
      (8, 'v1', 'v2', 'v3',),
      (8, 'v2', 'v3', 'v4',),
      (8, 'v0', 'v1', 'v2',),
      (8, 'v1', 'v2', 'v3',),
    ]
    vars = ('v0', 'v1', 'v2', 'v3', 'v4')
    c_p = relation.Problem(vars, clauses)
    fsat, values = c_p.solve()

    self.assertEquals(fsat, 5)
    self.assertTrue(values in [
        [1, 1, 1, 0, 1],
        [1, 1, 1, 0, 0],
        [0, 1, 1, 0, 1],
        [0, 1, 1, 0, 0],
    ])

if __name__ == '__main__':
  unittest.main()
