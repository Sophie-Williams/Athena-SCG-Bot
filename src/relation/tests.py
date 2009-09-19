import relation
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
    self.assertEqual(relation.is_irrelevant(123123, 3, 2), 0)

  def test_num_relevant_variables(self):
    self.assertEqual(relation.num_relevant_variables(123123, 3), 3)

  def test_is_forced(self):
    self.assertEqual(relation.is_forced(123123, 3, 1), -1)

  def test_first_forced_variable(self):
    self.assertEqual(relation.first_forced_variable(123123, 3, 0), None)

  def test_n_map(self):
    self.assertEqual(relation.n_map(123123, 3, 1), 252)

  def test_reduce(self):
    self.assertEqual(relation.reduce(123123, 3, 1, 0), 255)

  def test_swap(self):
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
    pass

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


if __name__ == '__main__':
  unittest.main()
