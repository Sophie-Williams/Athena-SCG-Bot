
import urllib2

def get_current_players(host, port):
    """Return a list of the names of the current players."""
    res = urllib2.urlopen('http://%s:%d/status' % (host, port))
    players = []
    for x in res.readlines():
        if x.find('class="current"') > 0:
            x = x[x.find('<td>')+4:]
            players.append(x[:x.find('</td>')])
    return filter(lambda x: x != 'Current Turn', players)

# HOST = 'cs4500tmp.ccs.neu.edu'
# PORT = 7007
# print get_current_players(HOST, PORT)
