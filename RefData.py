# -*- coding: utf-8 -*-
"""
Created on Sun Oct 20 22:09:45 2019

@author: darp_lord
"""
import os
import pandas as pd
import numpy as np
from AQI_Calc import aqicalc

class ReFdat:
	def __init__(self,filename):
		DFs=[]
		for file in os.listdir("./Reference_Monitor_Data"):
		    if file.endswith(".csv"):
		        print(os.path.join("./Reference_Monitor_Data", file))
		        DFs.append(pd.read_csv(os.path.join("./Reference_Monitor_Data", file)))
		self.df=pd.concat(DFs,ignore_index=True)
	def getDat(self):
		Coords=pd.DataFrame({
				'Lat':self.df['Latitude'],
				'Long':self.df['Longitude']
				})
	def cleanDat(self,df,valCol,minVal):
		df=df[df[valCol]>minVal]
		return df
	def getSingleTOP(self,ColName,ParamName,TOPn=5):
		self.df=self.cleanDat(self.df,'value',0)

		self.df[['Latitude','Longitude']]=(self.df['coordinates'].str.replace(r"[a-zA-Z{}=]",'')).str.split(", ",expand=True)

		self.df[['Latitude','Longitude']]=self.df[['Latitude','Longitude']].apply(pd.to_numeric)

		self.df[['Date','Time']]=((self.df['date'].str.split(",",expand=True)[0]).str.replace('[A-SU-Za-z={]','')).str.split("T",expand=True)

		dfParam=self.df[['Date','Time','Latitude','Longitude','parameter','value','unit']]

		gb=dfParam.groupby(['parameter'])
		dfL=[gb.get_group(x) for x in gb.groups]

		del dfParam

		dfD={}
		for i in dfL:
		    dfD[i['parameter'].values[0].upper()]=i.groupby(['Date','Latitude','Longitude'],as_index=False).mean()

		del dfL

		for i in dfD:
			dfD[i]['value']=dfD[i]['value'].apply(aqicalc,args=[i])
		dfParam=pd.concat(dfD)
		dfParam=dfParam.groupby(['Date','Latitude','Longitude'],as_index=False).max()
		dfParam=dfParam.groupby(['Latitude','Longitude']).tail(TOPn).reset_index(drop=True)

		dfParam['value']=dfParam['value'].round(5)
		return dfParam

if __name__=='__main__':
	AQS=ReFdat('./AOD_DATA/Addis.csv')
	Top5dat=AQS.getSingleTOP('Parameter Name','Carbon monoxide')
	print(Top5dat)
	Top5dat.to_csv('RefDat.csv',index=False)