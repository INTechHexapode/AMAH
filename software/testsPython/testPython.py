import serial
import time
import math

def angleToPulse(angle):
	return 10*angle + 1500

class Hexapod:
	def __init__(self, legs):
		self.legs = legs
		
	def setIdlePosition(self):
		for leg in self.legs:
			leg.setIdlePosition()
			
		time.sleep(1)
			
		self.disable()
			
	def disable(self):
		x = ""
		while x != ".":
			serial.write('Q\r')
			x = serial.read()
			
			
		for leg in self.legs:
			leg.disable()
	
	def setReady(self):
		for leg in legs:
			leg.setReady()
		
	
	def turnRight(self, angle):
		self.setReady()
		it = angle//36
		rest = angle%36
		for i in range(it):
			self.turn(1300, 1700)
			
		hipBodyPulseEnd = angleToPulse(rest)
		hipBodyPulseStart = 1300
		self.turn(hipBodyPulseStart, hipBodyPulseEnd)
	
	def turnLeft(self, angle):
		self.setReady()
		it = angle//36
		rest = angle%36
		for i in range(it):
			self.turn(1700, 1300)
		
		hipBodyPulseEnd = angleToPulse(rest)
		hipBodyPulseStart = 1300
		self.turn(hipBodyPulseEnd, hipBodyPulseStart)
	
	
	
	def walk(self):
		pulsemin = 1200
		pulsemax = 1500
		while 1:
			self.setReady()	
			self.moveListLegs([legs[0], legs[4]], pulsemin, 1900, 1500)
			self.moveListLegs([legs[3]], pulsemax, 1900, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[0], legs[4]], pulsemin, 1400, 1500)
			self.moveListLegs([legs[3]], pulsemax, 1400, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[1], legs[5]], pulsemax, 1900, 1500)
			self.moveListLegs([legs[2]], pulsemin, 1900, 1500)
			
			self.moveListLegs([legs[0], legs[4]], pulsemax, 1400, 1500)
			self.moveListLegs([legs[3]], pulsemin, 1400, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[1], legs[5]], pulsemax, 1400, 1500)
			self.moveListLegs([legs[2]], pulsemin, 1400, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[1], legs[5]], pulsemin, 1400, 1500)
			self.moveListLegs([legs[2]], pulsemax, 1400, 1500)
		
		
	def turn(self, hipBodyPulseStart, hipBodyPulseEnd):
		self.moveListLegs([legs[0], legs[3], legs[4]], hipBodyPulseStart, 1900, 1500)
		time.sleep(0.1)
		self.moveListLegs([legs[0], legs[3], legs[4]], hipBodyPulseStart, 1900, 1500)
		time.sleep(0.1)
		self.moveListLegs([legs[0], legs[3], legs[4]], hipBodyPulseStart, 1400, 1500)
		time.sleep(0.1)
		self.moveListLegs([legs[1], legs[2], legs[5]], hipBodyPulseStart, 1900, 1500)
		self.moveListLegs([legs[0], legs[3], legs[4]], hipBodyPulseEnd, 1400, 1500)
		
		self.moveListLegs([legs[1], legs[2], legs[5]], hipBodyPulseStart, 1900, 1500)
		time.sleep(0.1)
		self.moveListLegs([legs[1], legs[2], legs[5]], hipBodyPulseStart, 1900, 1500)
		time.sleep(0.1)
		self.moveListLegs([legs[1], legs[2], legs[5]], hipBodyPulseStart, 1400, 1500)
		time.sleep(0.1)
		self.moveListLegs([legs[0], legs[3], legs[4]], hipBodyPulseStart, 1900, 1500)
		self.moveListLegs([legs[1], legs[2], legs[5]], hipBodyPulseEnd, 1400, 1500)
		self.moveListLegs([legs[0], legs[3], legs[4]], hipBodyPulseEnd, 1400, 1500)
		time.sleep(0.1)
		
	def moveListLegs(self, legs, angle1, angle2, angle3):
		for leg in legs:
			leg.move(angle1, angle2, angle3)
			
	def stand(self):
		for it in range(5):
			self.moveListLegs([legs[1], legs[2], legs[5]], 1400, 1900, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[1], legs[2], legs[5]], 1400, 1400, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[0], legs[3], legs[4]], 1400, 1900, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[0], legs[3], legs[4]], 1400, 1400, 1500)
			time.sleep(0.1)
	
	def lieDown(self):
		for it in range(6):
			self.moveListLegs([legs[1], legs[2], legs[5]], 1400, 1900, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[1], legs[2], legs[5]], 1400, 1400 + it*100, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[0], legs[3], legs[4]], 1400, 1900, 1500)
			time.sleep(0.1)
			self.moveListLegs([legs[0], legs[3], legs[4]], 1400, 1400 + it*100, 1500)
			time.sleep(0.1)
		

class Servomotor:
	def __init__(self, idleAngle, sleepAngle, ID):
		self.minAngle = 1100
		self.maxAngle = 2000
		self.sleepAngle = sleepAngle
		self.idleAngle = idleAngle
		self.speed = 500
		self.ID = ID
		self.angle=idleAngle
	
	def move(self, angle):
		if(self.minAngle > angle):
			angle = self.minAngle
		if(self.maxAngle < angle):
			angle = self.maxAngle
		self.angle = angle
		serial.write('#' + str(self.ID) + ' P' + str(angle) + '\r')
	
	def disable(self):
		serial.write('#' + str(self.ID) + 'L\r')
		
	def setReady(self):
		self.move(self.angle)
		
		
class Leg:
	def __init__(self, alphaServo, betaServo, etaServo):
		self.alphaServo = alphaServo
		self.betaServo = betaServo
		self.etaServo = etaServo 
		self.alpha = 0
		self.beta = 0
		self.eta = 0 
		self.x = 0
		self.y = 0
		self.z = 0
		self.coxa = 30
		self.femur = 60
		self.tibia = 150
		self.zoffset = 120
	
	def goTo(self, x, y, z):
		self.alpha = math.degrees(math.atan2(x,y))
		L = math.hypot(z,(y - self.coxa))
		
		beta1 = math.acos(z/L)
		beta2 = math.acos((math.pow(self.tibia,2) - math.pow(self.femur,2) - math.pow(L,2))/(-2*self.femur*L))
		self.beta = math.degrees(beta1 + beta2)
		
		self.eta = math.acos((math.pow(L,2) - math.pow(self.tibia,2) - math.pow(self.femur,2))/(-2*self.femur*self.tibia))
		self.eta = math.degrees(self.eta)
		self.x = x
		self.y = y
		self.z = z
		
		print("alpha : " + str(self.alpha) + " beta : " + str(self.beta) + " eta : " + str(self.eta))
		
	
	def setReady(self):
		self.alpha.setReady()
		self.beta.setReady()
		self.eta.setReady() 
		
	def move(self, hipAngleY, hipAngleX, kneeAngle):
		self.alphaServo.move(hipAngleY)
		self.betaServo.move(hipAngleX)
		self.etaServo.move(kneeAngle)
	
	def setIdlePosition(self):
		self.etaServo.move(self.eta.idleAngle)
		self.alphaServo.move(self.alpha.idleAngle)
		self.betaServo.move(self.beta.idleAngle)
		
	def disable(self):
		self.alphaServo.disable()
		self.betaServo.disable()
		self.etaServo.disable()
	
	def update(self):
		self.move(angleToPulse(self.alpha), angleToPulse(self.beta), angleToPulse(self.eta))
		
	def goUp(self):
		self.etaServo.move(1500)
		self.alphaServo.move(self.alpha.sleepAngle)
		self.betaServo.move(1900)
	
	def synchronizedMove(self, beta, alpha, eta):
		serial.write('#' + str(self.alphaServo.ID) + ' P' + str(beta) +
					 '#' + str(self.betaServo.ID) + ' P' + str(alpha) +
					 '#' + str(self.etaServo.ID) + ' P' + str(eta) +' T750\r')

serial = serial.Serial('/dev/ttyUSB0', 115200)
angle = 1200
kneeIdle = 1700
kneeSleep = 1500
hipLegSleep = 1450
servos = []
servo60 = Servomotor(1450, 1450, 0)
servo61 = Servomotor(hipLegSleep, 1700, 1)
servo62 = Servomotor(kneeSleep, kneeIdle, 2)
leg6 = Leg(servo60, servo61, servo62)

servo40 = Servomotor(1450, 1450, 8)
servo41 = Servomotor(hipLegSleep, 1700, 9)
servo42 = Servomotor(kneeSleep, kneeIdle, 10)
leg4 = Leg(servo40, servo41, servo42)

servo20 = Servomotor(1450, 1450, 13)
servo21 = Servomotor(hipLegSleep, 1700, 14)
servo22 = Servomotor(kneeSleep, kneeIdle, 15)
leg2 = Leg(servo20, servo21, servo22)

servo10 = Servomotor(1450, 1450, 29)
servo11 = Servomotor(hipLegSleep, 1700, 30)
servo12 = Servomotor(kneeSleep, kneeIdle, 31)
leg1 = Leg(servo10, servo11, servo12)

servo30 = Servomotor(1450, 1450, 25)
servo31 = Servomotor(hipLegSleep, 1700, 26)
servo32 = Servomotor(kneeSleep, kneeIdle, 27)
leg3 = Leg(servo30, servo31, servo32)

servo50 = Servomotor(1450, 1450, 17)
servo51 = Servomotor(hipLegSleep, 1700, 18)
servo52 = Servomotor(kneeSleep, kneeIdle, 19)
leg5 = Leg(servo50, servo51, servo52)

legs = []
legs.append(leg1)
legs.append(leg2)
legs.append(leg3)	
legs.append(leg4)
legs.append(leg5)
legs.append(leg6)	

hexapod = Hexapod(legs)
#hexapod.disable()
leg1.goTo(0, 50, 100)
leg1.update()
time.sleep(2)
hexapod.disable()


"""legs[0].wakeUp()
legs[4].wakeUp()
legs[3].wakeUp()"""
"""
for servo in servos:
	servo.move(1200)"""
	
serial.close()

