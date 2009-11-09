import relation
import gen
import unittest
import test_data

class TestRelation(unittest.TestCase):

  def test_pascal(self):
    self.assertEqual(relation.pascal(0, 0), 1)
    self.assertEqual(relation.pascal(25, 0), 1)
    self.assertEqual(relation.pascal(25, 25), 1)
    self.assertEqual(relation.pascal(7, 3), 35)

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
    self.assertEqual(relation.first_forced_variable(123123, 3, 0), -1)

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
    self.assertEqual(relation.swap(123123, 5, 1, 2), 116943)
    self.assertEqual(relation.swap(116943, 5, 1, 2), 123123)
    self.assertEqual(relation.swap(123123, 5, 0, 3), 127441)
    self.assertEqual(relation.swap(127441, 5, 0, 3), 123123)
    self.assertEqual(relation.swap(61905, 5, 0, 3), 57587)
    self.assertEqual(relation.swap(57587, 5, 0, 3), 61905)

  def test_renme(self):
    f = relation.renme
    # XXX what the fuck?
    self.assertEqual(f(7, 2, relation.SOURCE, [1, 2, 0]), 6)
    self.assertEqual(f(4, 5, relation.SOURCE, [1, 2, 0]), 4)

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

  def test_reduce_rns(self):
    f = relation.reduce_rns
    self.assertEqual(f([2, 22]), [2])
    self.assertEqual(f([22, 2]), [2])
    self.assertEqual(f([4, 22]), [4])
    self.assertEqual(f([22, 4]), [4])
    self.assertEqual(f([6, 22]), [6])
    self.assertEqual(f([22, 6]), [6])
    self.assertEqual(f([16, 22]), [16])
    self.assertEqual(f([22, 16]), [16])
    self.assertEqual(f([18, 22]), [18])
    self.assertEqual(f([22, 18]), [18])
    self.assertEqual(f([20, 22]), [20])
    self.assertEqual(f([22, 20]), [20])
    self.assertEqual(f([22, 22]), [22])
    self.assertEqual(f([22, 6, 2]), [2])
    self.assertEqual(f([2, 10, 26, 1, 3, 8]), [1, 2, 8])
    self.assertEqual(f([3, 7]), [3])

  def test_break_even(self):
    f = relation.break_even
    for rn in range(1, 256, 2):
      self.assertEqual(f(rn, 3), 1.0)
    for rn in range(128, 256, 1):
      self.assertEqual(f(rn, 3), 1.0)

    self.assertAlmostEqual(f(0, 3), 0.000000000000)
    self.assertAlmostEqual(f(2, 3), 0.148148148148)
    self.assertAlmostEqual(f(4, 3), 0.148148148148)
    self.assertAlmostEqual(f(6, 3), 0.296296296296)
    self.assertAlmostEqual(f(8, 3), 0.148148148148)
    self.assertAlmostEqual(f(10, 3), 0.250000000000)
    self.assertAlmostEqual(f(12, 3), 0.250000000000)
    self.assertAlmostEqual(f(14, 3), 0.384900179460)
    self.assertAlmostEqual(f(16, 3), 0.148148148148)
    self.assertAlmostEqual(f(18, 3), 0.296296296296)
    self.assertAlmostEqual(f(20, 3), 0.296296296296)
    self.assertAlmostEqual(f(22, 3), 0.444444444444)
    self.assertAlmostEqual(f(24, 3), 0.250000000000)
    self.assertAlmostEqual(f(26, 3), 0.384900179460)
    self.assertAlmostEqual(f(28, 3), 0.384900179460)
    self.assertAlmostEqual(f(30, 3), 0.528152947731)
    self.assertAlmostEqual(f(32, 3), 0.148148148148)
    self.assertAlmostEqual(f(34, 3), 0.250000000000)
    self.assertAlmostEqual(f(36, 3), 0.250000000000)
    self.assertAlmostEqual(f(38, 3), 0.384900179460)
    self.assertAlmostEqual(f(40, 3), 0.296296296296)
    self.assertAlmostEqual(f(42, 3), 0.384900179460)
    self.assertAlmostEqual(f(44, 3), 0.384900179460)
    self.assertAlmostEqual(f(46, 3), 0.500000000000)
    self.assertAlmostEqual(f(48, 3), 0.250000000000)
    self.assertAlmostEqual(f(50, 3), 0.384900179460)
    self.assertAlmostEqual(f(52, 3), 0.384900179460)
    self.assertAlmostEqual(f(54, 3), 0.528152947731)
    self.assertAlmostEqual(f(56, 3), 0.384900179460)
    self.assertAlmostEqual(f(58, 3), 0.500000000000)
    self.assertAlmostEqual(f(60, 3), 0.500000000000)
    self.assertAlmostEqual(f(62, 3), 0.631130309441)
    self.assertAlmostEqual(f(64, 3), 0.148148148148)
    self.assertAlmostEqual(f(66, 3), 0.250000000000)
    self.assertAlmostEqual(f(68, 3), 0.250000000000)
    self.assertAlmostEqual(f(70, 3), 0.384900179460)
    self.assertAlmostEqual(f(72, 3), 0.296296296296)
    self.assertAlmostEqual(f(74, 3), 0.384900179460)
    self.assertAlmostEqual(f(76, 3), 0.384900179460)
    self.assertAlmostEqual(f(78, 3), 0.500000000000)
    self.assertAlmostEqual(f(80, 3), 0.250000000000)
    self.assertAlmostEqual(f(82, 3), 0.384900179460)
    self.assertAlmostEqual(f(84, 3), 0.384900179460)
    self.assertAlmostEqual(f(86, 3), 0.528152947731)
    self.assertAlmostEqual(f(88, 3), 0.384900179460)
    self.assertAlmostEqual(f(90, 3), 0.500000000000)
    self.assertAlmostEqual(f(92, 3), 0.500000000000)
    self.assertAlmostEqual(f(94, 3), 0.631130309441)
    self.assertAlmostEqual(f(96, 3), 0.296296296296)
    self.assertAlmostEqual(f(98, 3), 0.384900179460)
    self.assertAlmostEqual(f(100, 3), 0.384900179460)
    self.assertAlmostEqual(f(102, 3), 0.500000000000)
    self.assertAlmostEqual(f(104, 3), 0.444444444444)
    self.assertAlmostEqual(f(106, 3), 0.528152947731)
    self.assertAlmostEqual(f(108, 3), 0.528152947731)
    self.assertAlmostEqual(f(110, 3), 0.631130309441)
    self.assertAlmostEqual(f(112, 3), 0.384900179460)
    self.assertAlmostEqual(f(114, 3), 0.500000000000)
    self.assertAlmostEqual(f(116, 3), 0.500000000000)
    self.assertAlmostEqual(f(118, 3), 0.631130309441)
    self.assertAlmostEqual(f(120, 3), 0.528152947731)
    self.assertAlmostEqual(f(122, 3), 0.631130309441)
    self.assertAlmostEqual(f(124, 3), 0.631130309441)
    self.assertAlmostEqual(f(126, 3), 0.750000000000)
    # XXX More than rank 3?

  def test_rn_counts(self):
    clauses = [
      (3,  1, 'v1', 'v2', 'v3'),
      (22, 1, 'v2', 'v3', 'v4'),
      (3,  1, 'v3', 'v4', 'v5'),
      (22, 1, 'v1', 'v2', 'v3'),
      (22, 1, 'v1', 'v2', 'v3'),
      (22, 1, 'v1', 'v2', 'v3'),
      (21, 1, 'v1', 'v2', 'v3'),
      (22, 1, 'v2', 'v3', 'v4'),
      (22, 1, 'v3', 'v4', 'v5'),
      (21, 1, 'v2', 'v3', 'v4'),
      (22, 1, 'v3', 'v4', 'v5'),
      (21, 1, 'v2', 'v3', 'v4'),
      (22, 1, 'v3', 'v4', 'v5'),
      (21, 1, 'v2', 'v3', 'v4'),
      (22, 1, 'v3', 'v4', 'v5'),
    ]
    vars = ('v1', 'v2', 'v3', 'v4', 'v5')
    c_p = relation.Problem(vars, clauses)
    counts = c_p.rn_counts()
    self.assertEquals(counts[3], 2)
    self.assertEquals(counts[21], 4)
    self.assertEquals(counts[22], 9)
    self.assertEquals(len(counts.keys()), 3)

  def test_evergreen(self):
    c_p = relation.Problem(test_data.vars1, test_data.clauses1)
    sat, x = c_p.evergreen()
    self.assertEquals(sat, 462)
    c_p = relation.Problem(test_data.vars5, test_data.clauses5)
    sat, x = c_p.evergreen()
    self.assertEquals(sat, 66)

  def test_solve(self):
    clauses = [
      (22, 1, 'v1', 'v2', 'v3'),
      (22, 1, 'v2', 'v3', 'v4'),
      (22, 1, 'v3', 'v4', 'v5'),
    ]
    vars = ('v1', 'v2', 'v3', 'v4', 'v5')
    c_p = relation.Problem(vars, clauses)

    sat, values = c_p.solve()

    self.assertEquals(sat, 3)
    self.assertTrue(values in [
        [0, 0, 1, 0, 0],
        [1, 0, 0, 1, 0],
        [1, 0, 0, 0, 1],
        [0, 1, 0, 0, 1],
    ])

    clauses = [
      (8, 1, 'v0', 'v1', 'v2',),
      (8, 1, 'v1', 'v2', 'v3',),
      (8, 1, 'v1', 'v2', 'v3',),
      (8, 1, 'v2', 'v3', 'v4',),
      (8, 1, 'v2', 'v3', 'v4',),
      (8, 1, 'v1', 'v2', 'v3',),
      (8, 1, 'v1', 'v2', 'v3',),
      (8, 1, 'v2', 'v3', 'v4',),
      (8, 1, 'v0', 'v1', 'v2',),
      (8, 1, 'v1', 'v2', 'v3',),
    ]
    vars = ('v0', 'v1', 'v2', 'v3', 'v4')
    c_p = relation.Problem(vars, clauses)
    sat, values = c_p.solve()

    self.assertEquals(sat, 5)
    self.assertTrue(values in [
        [1, 1, 1, 0, 1],
        [1, 1, 1, 0, 0],
        [0, 1, 1, 0, 1],
        [0, 1, 1, 0, 0],
    ])


class TestGen(unittest.TestCase):

  def test_max_vars(self):
    f = gen.max_vars

    self.assertEquals(f(2000, rank=3), 13)
    self.assertEquals(f(2700, rank=3), 14)
    self.assertEquals(f(2800, rank=3), 15)

if __name__ == '__main__':
  unittest.main()
