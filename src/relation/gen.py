

def permute3(vars):
    for i in range(0, vars - 2):
        for j in range(i + 1, vars - 1):
            for k in range(j + 1, vars):
                yield (i, j, k)


def var(n):
#     return ("AhundredandeightywerechallengedbyTravistodie"
#             "Bythelinethathedrewwithhisswordwhenthebattlewasnigh."
#             "Anymanthatwouldfighttothedeathcrossover"
#             "Buthimthatwouldlivebetterfly"
#             "Andoverthelinewentahundredandseventynine"
#             "HeySantaAnnawerekillingyoursoldiersbelow"
#             "ThatmenwhereevertheygowillremembertheAlamo"
#             "Bowielaydyinbuthispowderwasreadyanddry"
#             "FlatonhisbackBowiekilledhimafewinreply"
#             "AndyoungDavidCrockettwassinginandlaughin"
#             "Withgallantryfierceinhiseyes"
#             "ForGodandforfreedomamanmorethanwillintodie"
#             "HeySantaAnnawerekillingyoursoldiersbelow"
#             "thatmenwhereevertheygowillremembertheAlamo"
#             "Andthentheysentayoungscoutfromthebattlementsbloodyandloud"
#             "Withthewordsoffarewellfromagarrisonvaliantandproud"
#             "GrievenotlittledarlinmydyinifTexasissovereignandfree"
#             "Wellneversurrenderandeverwithlibertybe"
#             "HeySantaAnnawerekillingyoursoldiersbelow"
#             "thatmenwhereevertheygowillremembertheAlamo%d" % n)
    return 'v%d' % n

def problem(p_type, degree=100):
    y = ''
    for x in range(0, degree):
        y += '%s  ' % var(x)
    for i, j, k in permute3(degree):
        y += '(%d %s %s %s ) ' % (p_type, var(i), var(j), var(k))
    return y

if __name__ == '__main__':
  import time

  s = time.time()
  # print s
  print problem(60, degree=10)
  e = time.time()
  #print e
  # print e - s
