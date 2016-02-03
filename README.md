# COMAP-MCM-SpaceJunk
##Mathematical contest in modeling
![alt tag](http://s10.postimg.org/hi5e9q1g9/spacejunk.jpg)
##Summary
Space debris is a growing problem that could affect many generations to come. Currently there are upwards of 500,000 pieces of junk in orbit around the Earth, which rarely draw the attention of any lay-man. However, recently there have been significant collisions, as pointed out by our problem statement, which have highlighted the level of concern humanity must show towards the space junk problem.

To approach this problem we designed a discrete time and discrete space based model to better understand space debris behavior and the possible solutions we may implement to clean it up. We organize our space debris into a three dimensional cube, and then proceed to approximate its positions one second at a time. This is done by approximating orbits with small segments of parabolas, over and over and over again. In addition, we use our three dimensional cube structure to efficiently check for intersections between our objects.

The two primary solutions we attempted to test were fluid based devices meant to slow debris and lead it to fall into orbit, and net based devices meant to gather debris and bring it out of orbit in one large lump. Both of these devices appeared at first to be relatively inexpensive options which had a possible wide range of results. Unfortunately, due to the large number of computations required to simulate such a large amount of space junk, and the level of precision needed to accurately represent future positions even over a 15 day period (.01 second calculation increments) we were unable to successfully test for the effect of these tools.

From our content research we found that space lasers appear to be the most effective for spot removal of junk in order to protect existing satellites, however very expensive to build and operate. We feel that further simulation and testing of fluid and net based systems will lead to the best solution for space debris clean up.
