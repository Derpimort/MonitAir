# -*- coding: utf-8 -*-
"""
Created on Sun Oct 20 10:45:07 2019

@author: darp_lord
"""

import pandas as pd
import numpy as np

class AQSdat:
	def __init__(self,filename):
		self.df=pd.read_csv(filename)
	def getDat(self):
		Coords=pd.DataFrame({
				'Lat':self.df['Latitude'],
				'Long':self.df['Longitude']
				})
	def cleanDat(self,df,valCol, limCol,minVal):
		df=df[(df[valCol]<df[limCol])&(df[valCol]>minVal)]
		return df
	def getSingleTOP(self,ColName,ParamName,TOPn=5):
		dfParam=self.df[self.df[ColName]==ParamName]
		dfParam=self.cleanDat(dfParam,'Sample Measurement','Detection Limit',0)

		dfParam=dfParam[['Latitude','Longitude','Date Local','Sample Measurement']].groupby(['Date Local','Latitude','Longitude'],as_index=False).mean()
		dfParam=dfParam.groupby(['Latitude','Longitude']).tail(TOPn).reset_index(drop=True)

		dfParam['Sample Measurement']=dfParam['Sample Measurement'].round(5)
		return dfParam

if __name__=='__main__':
	AQS=AQSdat('./AirQualitySystem.csv')
	Top5dat=AQS.getSingleTOP('Parameter Name','Carbon monoxide')
	print(Top5dat)
	Top5dat.to_csv('TestF.csv',index=False)